package com.bok.krypto.messaging.consumer;

import com.bok.krypto.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderConsumer {

    @JmsListener(destination = "${active-mq.orders-queue}")
    public void onOrderMessage(Order order) {
        log.info("Received Message: " + order.toString());
    }
}
