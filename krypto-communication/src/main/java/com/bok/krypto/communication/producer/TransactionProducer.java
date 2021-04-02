package com.bok.krypto.communication.producer;

import com.bok.krypto.communication.messages.TransactionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TransactionProducer {

    @Autowired
    JmsTemplate jmsTemplate;

    @Value("${active-mq.transactions-queue}")
    private String transfersTopic;


    public void send(TransactionMessage transfer) {
        try {
            log.info("Attempting Send transfer to Topic: " + transfersTopic);
            jmsTemplate.convertAndSend(transfersTopic, transfer);
        } catch (Exception e) {
            log.error("Received Exception during send Message: ", e);
        }
    }

}
