package com.bok.krypto.service;

import com.bok.integration.krypto.dto.TransactionDTO;
import com.bok.krypto.helper.KryptoHelper;
import com.bok.krypto.helper.MarketHelper;
import com.bok.krypto.helper.UserHelper;
import com.bok.krypto.helper.WalletHelper;
import com.bok.krypto.model.Transaction;
import com.bok.krypto.model.User;
import com.bok.krypto.model.Wallet;
import com.bok.krypto.service.interfaces.MarketService;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MarketServiceImpl implements MarketService {

    @Autowired
    MarketHelper marketHelper;

    @Autowired
    WalletHelper walletHelper;

    @Autowired
    UserHelper userHelper;

    @Autowired
    KryptoHelper kryptoHelper;

    @Override
    public TransactionDTO buy(Long userId, String symbol, BigDecimal amount) {
        Preconditions.checkArgument(userHelper.existsById(userId), "User does not exist.");
        Preconditions.checkArgument(kryptoHelper.existsBySymbol(symbol), "This krypto does not exist.");
        Preconditions.checkArgument(amount.compareTo(BigDecimal.ZERO) <= 0, "Cannot BUY a negative amount.");

        User user = userHelper.findById(userId);
        Wallet wallet = walletHelper.findByUserIdAndSymbol(user.getId(), symbol);

        Transaction transaction = marketHelper.buy(user, wallet, amount);
        return new TransactionDTO(transaction.getId(), transaction.getUser().getId(), transaction.getType().name(), transaction.getAmount());
    }

    @Override
    public TransactionDTO sell(Long userId, String symbol, BigDecimal amount) {
        Preconditions.checkArgument(userHelper.existsById(userId), "User does not exist.");
        Preconditions.checkArgument(kryptoHelper.existsBySymbol(symbol), "This krypto does not exist.");
        Preconditions.checkArgument(amount.compareTo(BigDecimal.ZERO) <= 0, "Cannot SELL a negative amount.");

        User user = userHelper.findById(userId);
        Wallet wallet = walletHelper.findByUserIdAndSymbol(user.getId(), symbol);
        Transaction transaction = marketHelper.sell(user, wallet, amount);
        return new TransactionDTO(transaction.getId(), transaction.getUser().getId(), transaction.getType().name(), transaction.getAmount());
    }
}
