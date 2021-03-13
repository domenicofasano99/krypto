package com.bok.krypto;

import com.bok.krypto.messaging.producer.TransactionProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KryptoApplicationTests {

    @Autowired
    TransactionProducer transactionProducer;

    //@Autowired
    //OrderProducer orderProducer;

    @Test
    void contextLoads() {
    }

}
