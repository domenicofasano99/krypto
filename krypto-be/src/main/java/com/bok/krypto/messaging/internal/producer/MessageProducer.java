package com.bok.krypto.messaging.internal.producer;

import com.bok.integration.EmailMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageProducer {
    @Autowired
    JmsTemplate jmsTemplate;

    @Value("${active-mq.emails}")
    private String emailQueue;

    public void send(EmailMessage emailMessage) {
        try {
            log.info("Attempting Send transfer to Topic: " + emailQueue);
            jmsTemplate.convertAndSend(emailQueue, emailMessage);
        } catch (Exception e) {
            log.error("Received Exception during send Message: ", e);
        }
    }

}
