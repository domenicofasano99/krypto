package com.bok.krypto;

import com.bok.krypto.messaging.producer.OrderProducer;
import com.bok.krypto.messaging.producer.TransferProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KryptoApplicationTests {

    @Autowired
    TransferProducer transferProducer;

    @Autowired
    OrderProducer orderProducer;

    @Test
    void contextLoads() {
    }

    @Test
    public void transferProducerTest() {
    }

    @Test
    public void orderProducerTest() {
    }

}
