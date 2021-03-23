package com.bok.krypto.helper;

import com.bok.integration.EmailMessage;
import com.bok.integration.krypto.WalletDeleteRequestDTO;
import com.bok.integration.krypto.WalletDeleteResponseDTO;
import com.bok.integration.krypto.WalletInfoDTO;
import com.bok.integration.krypto.WalletsDTO;
import com.bok.integration.krypto.dto.WalletRequestDTO;
import com.bok.integration.krypto.dto.WalletResponseDTO;
import com.bok.krypto.exception.*;
import com.bok.krypto.messaging.messages.WalletMessage;
import com.bok.krypto.model.User;
import com.bok.krypto.model.Wallet;
import com.bok.krypto.repository.WalletRepository;
import com.bok.krypto.service.interfaces.MessageService;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class WalletHelper {

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    TransactionHelper transactionHelper;

    @Autowired
    UserHelper userHelper;

    @Autowired
    KryptoHelper kryptoHelper;

    @Autowired
    MessageService messageService;

    @Autowired
    MarketHelper marketHelper;


    public Wallet findById(UUID id) {
        return walletRepository.findById(id).orElseThrow(() -> new WalletNotFoundException("wallet not found"));
    }

    public Boolean existsById(UUID id) {
        return walletRepository.existsById(id);
    }

    public Wallet findByUserIdAndSymbol(Long userId, String symbol) {
        return walletRepository.findByUser_IdAndKrypto_Symbol(userId, symbol).orElseThrow(() -> new WalletNotFoundException("wallet not found"));

    }

    @Transactional
    public synchronized BigDecimal withdraw(Wallet wallet, BigDecimal amount) {
        if (wallet.getAvailableAmount().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("not enough funds to perform the withdrawal");
        }
        log.info("withdrawing {} from wallet {}", amount, wallet.getId());
        BigDecimal newBalance = wallet.getAvailableAmount().subtract(amount);
        wallet.setAvailableAmount(newBalance);
        walletRepository.saveAndFlush(wallet);
        log.info("wallet {} balance: {}", wallet.getId(), newBalance);
        return amount;
    }

    @Transactional
    public synchronized BigDecimal deposit(Wallet wallet, BigDecimal amount) {
        Wallet w = findById(wallet.getId());
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidRequestException("cannot deposit negative amounts");
        }
        log.info("depositing {} {} from wallet {}", amount, w.getKrypto().getSymbol(), wallet.getId());
        BigDecimal newBalance = w.getAvailableAmount().add(amount);
        w.setAvailableAmount(newBalance);
        walletRepository.saveAndFlush(w);
        log.info("wallet {} balance: {}", wallet.getId(), newBalance);
        return amount;
    }

    public Boolean existsByUserIdAndSymbol(Long userId, String symbol) {
        return walletRepository.existsByUser_IdAndKrypto_Symbol(userId, symbol);
    }

    public WalletResponseDTO createWallet(Long userId, WalletRequestDTO requestDTO) {
        if (!kryptoHelper.existsBySymbol(requestDTO.symbol)) {
            throw new KryptoNotFoundException("This krypto doesn't exists");
        }
        if (walletRepository.existsByUser_IdAndKrypto_Symbol(userId, requestDTO.symbol)) {
            throw new WalletAlreadyExistsException("A wallet with the same Krypto exists for this user");
        }
        Wallet w = new Wallet();
        w = walletRepository.save(w);
        WalletMessage walletMessage = new WalletMessage();
        walletMessage.id = w.getId();
        walletMessage.userId = userId;
        walletMessage.symbol = requestDTO.symbol;
        messageService.send(walletMessage);
        return new WalletResponseDTO(WalletResponseDTO.Status.ACCEPTED);
    }

    public void handleMessage(WalletMessage walletMessage) {
        Wallet w = walletRepository.findById(walletMessage.id)
                .orElseThrow(() -> new RuntimeException("This wallet should have been pre-persisted."));
        User u = userHelper.findById(walletMessage.userId);
        w.setUser(u);
        w.setKrypto(kryptoHelper.findBySymbol(walletMessage.symbol));
        walletRepository.save(w);
        messageService.send(emailWalletCreation(w, u));
    }

    private EmailMessage emailWalletCreation(Wallet w, User u) {
        EmailMessage email = new EmailMessage();
        email.subject = "BOK - Wallet creation";
        email.text = "Your wallet for Krypto " + w.getKrypto().getSymbol() + " has been created.";
        email.to = u.getEmail();
        return email;
    }

    public Boolean hasSufficientBalance(Long userId, String symbol, BigDecimal amount) {
        return walletRepository.existsByUser_IdAndKrypto_SymbolAndAvailableAmountGreaterThanEqual(userId, symbol, amount);
    }

    public WalletDeleteResponseDTO delete(Long userId, WalletDeleteRequestDTO deleteRequestDTO) {
        Preconditions.checkArgument(userHelper.existsById(userId));
        Preconditions.checkArgument(walletRepository.existsByUser_IdAndKrypto_Symbol(userId, deleteRequestDTO.symbol));
        Preconditions.checkNotNull(deleteRequestDTO.destinationIBAN);
        User user = userHelper.findById(userId);
        Wallet wallet = findByUserIdAndSymbol(userId, deleteRequestDTO.symbol);
        marketHelper.emptyWallet(user, wallet);
        walletRepository.delete(wallet);
        String to, subject, text;
        to = user.getEmail();
        subject = "BOK - Wallet deletion";
        text = "Your wallet " + wallet.getKrypto().getSymbol() + " has been deleted.";
        sendMarketEmail(subject, to, text);
        return new WalletDeleteResponseDTO(wallet.getId());
    }

    public void sendMarketEmail(String subject, String email, String text) {
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.subject = subject;
        emailMessage.to = email;
        emailMessage.text = text;
        messageService.send(emailMessage);
    }

    public WalletInfoDTO info(Long userId, UUID walletID) {
        Preconditions.checkArgument(userHelper.existsById(userId));
        Preconditions.checkArgument(walletRepository.existsById(walletID));

        Wallet wallet = findById(walletID);
        return getInfoFromWallet(wallet);
    }

    public WalletsDTO wallets(Long userId) {
        Preconditions.checkArgument(userHelper.existsById(userId));
        List<Wallet> wallets = walletRepository.findByUser_Id(userId);
        WalletsDTO walletsDTO = new WalletsDTO();
        walletsDTO.wallets = new ArrayList<>();
        for (Wallet w : wallets) {
            walletsDTO.wallets.add(getInfoFromWallet(w));
        }
        return walletsDTO;
    }

    private WalletInfoDTO getInfoFromWallet(Wallet wallet) {
        WalletInfoDTO info = new WalletInfoDTO();
        info.availableAmount = wallet.getAvailableAmount();
        info.symbol = wallet.getKrypto().getSymbol();
        info.creationTimestamp = wallet.getCreationTime();
        info.updateTimestamp = wallet.getUpdateTime();
        return info;
    }
}
