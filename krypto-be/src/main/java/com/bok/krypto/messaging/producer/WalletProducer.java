package com.bok.krypto.messaging.producer;

import com.bok.krypto.messaging.messages.WalletCreationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WalletProducer {
    @Autowired
    JmsTemplate jmsTemplate;

    @Value("${queue.wallet.creation}")
    private String walletCreationQueue;

    public void send(WalletCreationMessage walletCreationMessage) {
        try {
            log.info("Sending wallet creation to queue:{} ", walletCreationQueue);
            jmsTemplate.convertAndSend(walletCreationQueue, walletCreationMessage);
        } catch (Exception e) {
            log.error("Received Exception during send Message: ", e);
        }
    }
}
