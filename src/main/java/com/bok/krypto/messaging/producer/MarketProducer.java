package com.bok.krypto.messaging.producer;

import com.bok.krypto.messaging.messages.MarketMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MarketProducer {
    @Autowired
    JmsTemplate jmsTemplate;

    @Value("${active-mq.market-queue}")
    private String marketQueue;


    public void send(MarketMessage marketMessage) {
        try {
            log.info("Attempting Send transfer to Queue: " + marketQueue);
            jmsTemplate.convertAndSend(marketQueue, marketMessage);
        } catch (Exception e) {
            log.error("Received Exception during send Message: ", e);
        }
    }
}
