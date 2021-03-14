package com.bok.krypto.utils;

import com.bok.krypto.helper.KryptoHelper;
import com.bok.krypto.model.Krypto;
import com.bok.krypto.model.User;
import com.bok.krypto.model.Wallet;
import com.bok.krypto.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;

@Component
@Slf4j
public class ModelTestUtils {

    @Autowired
    HistoricalDataRepository historicalDataRepository;

    @Autowired
    KryptoHelper kryptoHelper;

    @Autowired
    KryptoRepository kryptoRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    TransactionRepository transactionRepository;

    private Random random = new Random();

    public void populateDB() {
        kryptoRepository.save(new Krypto("Bitcoin", "BTC", new BigDecimal(50000)));
        kryptoRepository.save(new Krypto("Ethereum", "ETH", new BigDecimal(1800)));

    }

    public void clearAll() {
        transactionRepository.deleteAll();
        historicalDataRepository.deleteAll();
        kryptoRepository.deleteAll();
        walletRepository.deleteAll();
        userRepository.deleteAll();
    }

    public User createUser() {
        //Id for this class should reflect parent User ids
        User u = new User();
        u.setId(randomID());
        log.info("Created user with id: {}", u.getId());
        return userRepository.save(u);
    }

    public Long randomID() {
        return System.nanoTime();
    }

    public Long randomLong() {
        return random.nextLong();
    }

    public Wallet createWallet(User user, String kryptoSymbol) {
        Wallet w = new Wallet();
        w.setUser(user);
        Krypto k = kryptoHelper.findBySymbol(kryptoSymbol);
        w.setKrypto(k);
        w = walletRepository.save(w);
        log.info("Created wallet with id: {}", w.getId());
        return w;
    }
}
