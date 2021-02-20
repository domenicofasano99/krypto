package com.bok.krypto.messaging.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class TransferProducer {
/*
    private static final Logger log = LoggerFactory.getLogger(TransferProducer.class);

    @Autowired
    JmsTemplate jmsTemplate;

    @Value("${active-mq.transfers-queue}")
    private String transfersTopic;


    public void send(Transfer transfer) {
        try {
            log.info("Attempting Send transfer to Topic: " + transfersTopic);
            jmsTemplate.convertAndSend(transfersTopic, transfer);
        } catch (Exception e) {
            log.error("Received Exception during send Message: ", e);
        }
    }

 */
}
