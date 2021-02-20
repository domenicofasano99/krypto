package com.bok.krypto.helper;

import com.bok.krypto.core.ExchangeLogic;
import com.bok.krypto.model.Krypto;
import com.bok.krypto.model.Transaction;
import com.bok.krypto.model.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class ExchangeHelper {

    ExchangeLogic logic = new ExchangeLogic();

    @Autowired
    KryptoHelper kryptoHelper;

    @Autowired
    WalletHelper walletHelper;

    public Transaction buy(BigDecimal amount, String code, UUID walletID) {
        Krypto k = kryptoHelper.findByCode(code);
        Wallet w = walletHelper.findById(walletID);


    }

    public Transaction sell(BigDecimal amount, String code, UUID walletID) {

    }

    public Transaction transfer(UUID from, UUID to, BigDecimal amount) {
        return walletHelper.transfer(from, to, amount);
    }
}
