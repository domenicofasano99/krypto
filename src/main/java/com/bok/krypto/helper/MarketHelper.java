package com.bok.krypto.helper;

import com.bok.krypto.exception.InsufficientBalanceException;
import com.bok.krypto.model.Transaction;
import com.bok.krypto.model.User;
import com.bok.krypto.model.Wallet;
import com.bok.krypto.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MarketHelper {

    @Autowired
    WalletHelper walletHelper;

    @Autowired
    KryptoHelper kryptoHelper;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserHelper userHelper;


    public Transaction buy(User user, Wallet wallet, BigDecimal amount) {
        Transaction t = new Transaction(user, Transaction.Type.BUY, Transaction.Status.PENDING, null, wallet, amount);
        transactionRepository.save(t);
        walletHelper.deposit(wallet, amount);
        return t;
    }

    public Transaction sell(User user, Wallet wallet, BigDecimal amount) {
        if (wallet.getAvailableAmount().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("wallet does not have a sufficient balance");
        }
        Transaction t = new Transaction(user, Transaction.Type.SELL, Transaction.Status.PENDING, wallet, null, amount);
        transactionRepository.save(t);
        walletHelper.withdraw(wallet, amount);
        return t;
    }

}
