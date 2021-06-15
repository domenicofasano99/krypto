package com.bok.krypto.messaging.producer;

import com.bok.krypto.messaging.messages.TransferMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TransferProducer {

    @Autowired
    JmsTemplate jmsTemplate;

    @Value("${queue.transfers}")
    private String transfersTopic;

    public void send(TransferMessage transferMessage) {
        try {
            log.info("Attempting Send transfer to Topic: " + transferMessage);
            jmsTemplate.convertAndSend(transfersTopic, transferMessage);
        } catch (Exception e) {
            log.error("Received Exception during send Message: ", e);
        }
    }
}
