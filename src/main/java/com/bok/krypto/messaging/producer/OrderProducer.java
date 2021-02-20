package com.bok.krypto.messaging.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderProducer {

    private static final Logger log = LoggerFactory.getLogger(OrderProducer.class);

    @Autowired
    JmsTemplate jmsTemplate;

    @Value("${active-mq.orders-queue}")
    private String ordersQueue;


    public void send(Order order) {
        try {
            log.info("Attempting Send transfer to Topic: " + ordersQueue);
            jmsTemplate.convertAndSend(ordersQueue, order);
        } catch (Exception e) {
            log.error("Received Exception during send Message: ", e);
        }
    }

}
