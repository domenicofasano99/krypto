package com.bok.krypto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KryptoApplicationTests {

    @Autowired
    TransactionProducer transactionProducer;

    @Test
    void contextLoads() {
    }

}
