package com.bok.krypto.messaging;

import com.bok.krypto.model.Transfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;


@Component
public class MessageProducer {

    private static final Logger log = LoggerFactory.getLogger(MessageProducer.class);
    @Autowired
    JmsTemplate jmsTemplate;

    @Value("${active-mq.transfers-topic}")
    private String topic;

    public void sendMessage(Transfer transfer) {
        try {
            log.info("Attempting Send transfer to Topic: " + topic);
            jmsTemplate.convertAndSend(topic, transfer);
        } catch (Exception e) {
            log.error("Received Exception during send Message: ", e);
        }
    }
}
