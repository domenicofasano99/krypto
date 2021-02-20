package com.bok.krypto.helper;

import com.bok.krypto.exception.InsufficientBalanceException;
import com.bok.krypto.exception.WalletNotFoundException;
import com.bok.krypto.model.Transaction;
import com.bok.krypto.model.Wallet;
import com.bok.krypto.repository.TransactionRepository;
import com.bok.krypto.repository.WalletRepository;
import com.sun.jdi.request.InvalidRequestStateException;
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


    public Wallet findById(UUID id) {
        return walletRepository.findById(id).orElseThrow(() -> new WalletNotFoundException("wallet not found"));
    }

    @Transactional
    public BigDecimal withdraw(UUID walletId, BigDecimal amount) {
        Wallet w = findById(walletId);
        if (w.getAvailableAmount().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("not enough funds to perform the withdrawal");
        }
        log.info("withdrawing {} from wallet {}", amount, walletId);
        BigDecimal newBalance = w.getAvailableAmount().subtract(amount);
        w.setAvailableAmount(newBalance);
        walletRepository.saveAndFlush(w);
        log.info("wallet {} balance: {}", walletId, newBalance);
        return amount;
    }

    @Transactional
    public BigDecimal deposit(UUID walletId, BigDecimal amount) {
        Wallet w = findById(walletId);
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidRequestStateException("cannot deposit negative amounts");
        }
        log.info("depositing {} {} from wallet {}", amount, w.getKrypto().getCode(), walletId);
        BigDecimal newBalance = w.getAvailableAmount().add(amount);
        w.setAvailableAmount(newBalance);
        walletRepository.saveAndFlush(w);
        log.info("wallet {} balance: {}", walletId, newBalance);
        return amount;
    }

    @Transactional
    public Transaction transfer(UUID from, UUID to, BigDecimal amount) {
        Wallet source = findById(from);
        Wallet destination = findById(to);
        withdraw(from, amount);
        deposit(to, amount);
        log.info("transferred {} from {} to {}", amount, from, to);
        return transactionRepository.save(new Transaction(Transaction.Type.TRANSFER, source, destination, amount));
    }
}
