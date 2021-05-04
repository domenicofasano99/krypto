package com.bok.krypto.messaging.internal.producer;

import com.bok.krypto.messaging.internal.messages.WalletMessage;
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

    @Value("${active-mq.wallets}")
    private String walletsQueue;

    public void send(WalletMessage walletMessage) {
        try {
            log.info("Attempting Send transfer to Topic: " + walletsQueue);
            jmsTemplate.convertAndSend(walletsQueue, walletMessage);
        } catch (Exception e) {
            log.error("Received Exception during send Message: ", e);
        }
    }
}
