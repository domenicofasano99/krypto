package com.bok.krypto.helper;

import com.bok.integration.EmailMessage;
import com.bok.integration.krypto.dto.WalletRequestDTO;
import com.bok.integration.krypto.dto.WalletResponseDTO;
import com.bok.krypto.exception.InsufficientBalanceException;
import com.bok.krypto.exception.InvalidRequestException;
import com.bok.krypto.exception.WalletAlreadyExistsException;
import com.bok.krypto.exception.WalletNotFoundException;
import com.bok.krypto.messaging.WalletMessage;
import com.bok.krypto.model.User;
import com.bok.krypto.model.Wallet;
import com.bok.krypto.repository.TransactionRepository;
import com.bok.krypto.repository.WalletRepository;
import com.bok.krypto.service.interfaces.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Component
public class WalletHelper {

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserHelper userHelper;

    @Autowired
    KryptoHelper kryptoHelper;

    @Autowired
    MessageService messageService;


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
    public BigDecimal withdraw(Wallet wallet, BigDecimal amount) {
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
    public BigDecimal deposit(Wallet wallet, BigDecimal amount) {
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

    public WalletResponseDTO createWallet(WalletRequestDTO requestDTO) {
        if (walletRepository.existsByUser_IdAndKrypto_Symbol(requestDTO.userId, requestDTO.symbol)) {
            throw new WalletAlreadyExistsException("A wallet with the same Krypto exists for this user");
        }
        WalletMessage walletMessage = new WalletMessage();
        walletMessage.userId = requestDTO.userId;
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
}
