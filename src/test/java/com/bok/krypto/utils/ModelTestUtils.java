package com.bok.krypto.utils;

import com.bok.krypto.helper.KryptoHelper;
import com.bok.krypto.helper.TransferHelper;
import com.bok.krypto.model.*;
import com.bok.krypto.repository.*;
import com.bok.krypto.service.interfaces.TransferService;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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

    @Autowired
    TransferHelper transferHelper;

    @Autowired
    TransferService transferService;


    public static final Random random = new Random();
    public static final Faker faker = new Faker();

    public synchronized void populateDB() {

        kryptoRepository.save(new Krypto("Bitcoin", "BTC", new BigDecimal(50000)));
        kryptoRepository.save(new Krypto("Ethereum", "ETH", new BigDecimal(1800)));
        kryptoRepository.save(new Krypto("Litecoin", "LTC", new BigDecimal(1800)));
        kryptoRepository.save(new Krypto("Cardano", "ADA", new BigDecimal(1800)));
        kryptoRepository.save(new Krypto("DogeCoin", "DOGE", new BigDecimal(1800)));

    }

    public synchronized void clearAll() {
        historicalDataRepository.deleteAll();
        transactionRepository.deleteAll();
        walletRepository.deleteAll();
        kryptoRepository.deleteAll();
        userRepository.deleteAll();

    }

    public void generateHistoricalDataForeachKrypto() {
        List<Krypto> kryptos = kryptoRepository.findAll();
        for (Krypto k : kryptos) {
            generateRandomHistoricalData(k, Instant.parse("2007-12-03T10:15:30.00Z"), Instant.now(), 30);
        }
    }

    public User createUser(Long userId) {
        User u = new User();
        u.setId(userId);
        return userRepository.save(u);
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


    public Wallet createWallet(User user, Krypto krypto, BigDecimal baseAmount) {
        Wallet w = new Wallet();
        w.setKrypto(krypto);
        w.setUser(user);
        w.setAvailableAmount(baseAmount);
        return walletRepository.saveAndFlush(w);
    }

    public Krypto getKrypto(String krypto) {
        return kryptoRepository.findBySymbol(krypto).get();
    }

    public Krypto getRandomKrypto() {
        List<Krypto> kryptoSet = kryptoRepository.findAll();
        long random = ThreadLocalRandom.current().nextLong(0, kryptoSet.size());
        return kryptoSet.get(Math.toIntExact(random));
    }

    public void generateRandomHistoricalData(Krypto krypto, Instant start, Instant end, Integer numberOfRecords) {
        List<HistoricalData> list = new ArrayList<>();
        for (long c = 0; c < numberOfRecords; c++) {
            HistoricalData datum = new HistoricalData();
            datum.setRecordTimestamp(between(start, end));
            datum.setPrice(faker.number().randomDouble(5, 100, 250));
            datum.setKrypto(krypto);
            list.add(datum);
        }
        historicalDataRepository.saveAll(list);
    }

    private static Instant between(Instant startInclusive, Instant endExclusive) {
        long startSeconds = startInclusive.getEpochSecond();
        long endSeconds = endExclusive.getEpochSecond();
        long random = ThreadLocalRandom
                .current()
                .nextLong(startSeconds, endSeconds);

        return Instant.ofEpochSecond(random);
    }

    public void generateDatabaseRandomNoise(Integer numOfKryptos, Integer recordsPerKrypto) {
        for (int c = 0; c < numOfKryptos; c++) {
            //User u = createUser();
            Krypto k = getRandomKrypto();
            generateRandomHistoricalData(k, Instant.EPOCH, Instant.now(), recordsPerKrypto);
            //Wallet w = createWallet(u, k.getSymbol());
        }
    }


    public Boolean allTransfersProcessed() {
        List<Transaction> pendingList = transactionRepository.findAllPendingTransfers();
        return pendingList.size() == 0;
    }

    public Boolean allWalletsProcessed() {
        List<Wallet> pendingList = walletRepository.findAllPendingWallets();
        return pendingList.size() == 0;
    }
}
