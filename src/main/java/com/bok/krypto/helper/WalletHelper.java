package com.bok.krypto.helper;

import com.bok.krypto.exception.InsufficientBalanceException;
import com.bok.krypto.exception.InvalidRequestException;
import com.bok.krypto.exception.WalletNotFoundException;
import com.bok.krypto.model.Transaction;
import com.bok.krypto.model.User;
import com.bok.krypto.model.Wallet;
import com.bok.krypto.repository.TransactionRepository;
import com.bok.krypto.repository.WalletRepository;
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


    public Wallet findById(UUID id) {
        return walletRepository.findById(id).orElseThrow(() -> new WalletNotFoundException("wallet not found"));
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

    @Transactional
    public Transaction transfer(User user, Wallet source, Wallet destination, BigDecimal amount) {
        withdraw(source, amount);
        deposit(destination, amount);
        log.info("transferred {} from {} to {}", amount, source.getId(), destination.getId());
        return transactionRepository.save(new Transaction(user, Transaction.Type.TRANSFER, source, destination, amount));
    }

}
