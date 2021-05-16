package com.bok.krypto.helper;

import com.bok.krypto.exception.InsufficientBalanceException;
import com.bok.krypto.exception.InvalidRequestException;
import com.bok.krypto.exception.KryptoNotFoundException;
import com.bok.krypto.exception.WalletAlreadyExistsException;
import com.bok.krypto.exception.WalletNotFoundException;
import com.bok.krypto.integration.internal.dto.WalletDeleteRequestDTO;
import com.bok.krypto.integration.internal.dto.WalletDeleteResponseDTO;
import com.bok.krypto.integration.internal.dto.WalletInfoDTO;
import com.bok.krypto.integration.internal.dto.WalletRequestDTO;
import com.bok.krypto.integration.internal.dto.WalletResponseDTO;
import com.bok.krypto.integration.internal.dto.WalletsDTO;
import com.bok.krypto.messaging.internal.messages.WalletMessage;
import com.bok.krypto.model.Account;
import com.bok.krypto.model.Wallet;
import com.bok.krypto.repository.WalletRepository;
import com.bok.krypto.service.bank.BankService;
import com.bok.krypto.service.interfaces.MessageService;
import com.bok.parent.integration.message.EmailMessage;
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
    AccountHelper accountHelper;

    @Autowired
    KryptoHelper kryptoHelper;

    @Autowired
    MessageService messageService;

    @Autowired
    MarketHelper marketHelper;

    @Autowired
    BankService bankService;


    public Wallet findByPublicId(String publicId) {
        return walletRepository.findByPublicId(publicId).orElseThrow(() -> new WalletNotFoundException("wallet not found"));
    }

    public Boolean existsByPublicId(String publicId) {
        return walletRepository.existsByPublicId(publicId);
    }

    public Wallet findByAccountIdAndSymbol(Long userId, String symbol) {
        return walletRepository.findByAccount_IdAndKrypto_Symbol(userId, symbol).orElseThrow(() -> new WalletNotFoundException("wallet not found"));

    }

    @Transactional
    public BigDecimal withdraw(Wallet wallet, BigDecimal amount) {
        if (wallet.getAvailableAmount().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("not enough funds to perform the withdrawal");
        }
        log.info("withdrawing {} from wallet {}", amount, wallet.getPublicId());
        BigDecimal newBalance = wallet.getAvailableAmount().subtract(amount);
        wallet.setAvailableAmount(newBalance);
        walletRepository.saveAndFlush(wallet);
        log.info("wallet {} balance: {}", wallet.getPublicId(), newBalance);
        return amount;
    }

    @Transactional
    public BigDecimal deposit(Wallet wallet, BigDecimal amount) {
        Wallet w = findByPublicId(wallet.getPublicId());
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidRequestException("Cannot deposit negative amounts");
        }
        log.info("depositing {} {} to wallet {}", amount, w.getKrypto().getSymbol(), wallet.getPublicId());
        BigDecimal newBalance = w.getAvailableAmount().add(amount);
        w.setAvailableAmount(newBalance);
        walletRepository.saveAndFlush(w);
        log.info("wallet {} balance: {}", wallet.getPublicId(), newBalance);
        return amount;
    }

    public Boolean existsByUserIdAndSymbol(Long userId, String symbol) {
        return walletRepository.existsByAccount_IdAndKrypto_Symbol(userId, symbol);
    }

    public WalletResponseDTO createWallet(Long userId, WalletRequestDTO requestDTO) {
        if (!kryptoHelper.existsBySymbol(requestDTO.symbol)) {
            throw new KryptoNotFoundException("This krypto doesn't exists");
        }
        if (walletRepository.existsByAccount_IdAndKrypto_Symbol(userId, requestDTO.symbol)) {
            throw new WalletAlreadyExistsException("A wallet with the same Krypto exists for this user");
        }
        Wallet w = new Wallet();
        w = walletRepository.save(w);
        WalletMessage walletMessage = new WalletMessage();
        walletMessage.id = w.getId();
        walletMessage.accountId = userId;
        walletMessage.symbol = requestDTO.symbol;
        messageService.sendWallet(walletMessage);
        return new WalletResponseDTO(WalletResponseDTO.Status.ACCEPTED);
    }

    public void handleMessage(WalletMessage walletMessage) {
        Wallet w = walletRepository.findById(walletMessage.id)
                .orElseThrow(() -> new RuntimeException("This wallet should have been pre-persisted."));
        Account account = accountHelper.findById(walletMessage.accountId);
        w.setAccount(account);
        w.setPublicId(UUID.randomUUID().toString());
        w.setKrypto(kryptoHelper.findBySymbol(walletMessage.symbol));
        walletRepository.save(w);
        messageService.sendEmail(emailWalletCreation(w, account));
    }

    private EmailMessage emailWalletCreation(Wallet w, Account u) {
        EmailMessage email = new EmailMessage();
        email.subject = "BOK - Wallet creation";
        email.text = "Your wallet for Krypto " + w.getKrypto().getSymbol() + " has been created.";
        email.to = u.getEmail();
        return email;
    }

    public Boolean hasSufficientBalance(Long accountId, String symbol, BigDecimal amount) {
        return walletRepository.existsByAccount_IdAndKrypto_SymbolAndAvailableAmountGreaterThanEqual(accountId, symbol, amount);
    }

    public WalletDeleteResponseDTO delete(Long accountId, WalletDeleteRequestDTO deleteRequestDTO) {
        Preconditions.checkArgument(accountHelper.existsById(accountId));
        Preconditions.checkArgument(walletRepository.existsByAccount_IdAndKrypto_Symbol(accountId, deleteRequestDTO.symbol));
        Preconditions.checkNotNull(deleteRequestDTO.destinationIBAN);
        Account account = accountHelper.findById(accountId);
        Wallet wallet = findByAccountIdAndSymbol(accountId, deleteRequestDTO.symbol);
        marketHelper.emptyWallet(account, wallet);
        walletRepository.delete(wallet);
        String to, subject, text;
        to = account.getEmail();
        subject = "BOK - Wallet deletion";
        text = "Your wallet " + wallet.getKrypto().getSymbol() + " has been deleted.";
        sendMarketEmail(subject, to, text);
        return new WalletDeleteResponseDTO(wallet.getPublicId());
    }

    public void sendMarketEmail(String subject, String email, String text) {
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.subject = subject;
        emailMessage.to = email;
        emailMessage.text = text;
        messageService.sendEmail(emailMessage);
    }

    public WalletInfoDTO info(Long accountId, String walletID) {
        Preconditions.checkArgument(accountHelper.existsById(accountId));
        Preconditions.checkArgument(walletRepository.existsByPublicId(walletID));

        Wallet wallet = findByPublicId(walletID);
        return getInfoFromWallet(wallet);
    }

    public WalletsDTO wallets(Long accountId) {
        Preconditions.checkArgument(accountHelper.existsById(accountId));
        List<Wallet> wallets = walletRepository.findByAccount_Id(accountId);
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
