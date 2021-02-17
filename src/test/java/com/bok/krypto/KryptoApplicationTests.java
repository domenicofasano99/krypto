package com.bok.krypto;

import com.bok.krypto.messaging.producer.OrderProducer;
import com.bok.krypto.messaging.producer.TransferProducer;
import com.bok.krypto.model.Order;
import com.bok.krypto.model.Transfer;
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
        Transfer t = new Transfer();
        transferProducer.send(t);
    }

    @Test
    public void orderProducerTest() {
        Order o = new Order();
        orderProducer.send(o);
    }

}
