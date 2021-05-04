package com.bok.krypto.messaging.internal.producer;

import com.bok.krypto.messaging.internal.messages.PurchaseMessage;
import com.bok.krypto.messaging.internal.messages.SellMessage;
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

    @Value("${active-mq.market-purchase}")
    private String marketPurchaseQueue;

    @Value("${active-mq.market-sell}")
    private String marketSellQueue;

    public void send(PurchaseMessage purchaseMessage) {
        try {
            log.info("Sending purchaseMessage to Queue: " + marketPurchaseQueue);
            jmsTemplate.convertAndSend(marketPurchaseQueue, purchaseMessage);
        } catch (Exception e) {
            log.error("Received Exception during send Message: ", e);
        }
    }

    public void send(SellMessage sellMessage) {
        try {
            log.info("Sending sellMessage to Queue: " + marketSellQueue);
            jmsTemplate.convertAndSend(marketSellQueue, sellMessage);
        } catch (Exception e) {
            log.error("Received Exception during send Message: ", e);
        }
    }
}
