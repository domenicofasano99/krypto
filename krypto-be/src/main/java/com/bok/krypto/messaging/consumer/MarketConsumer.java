package com.bok.krypto.messaging.consumer;

import com.bok.krypto.messaging.messages.PurchaseMessage;
import com.bok.krypto.messaging.messages.SellMessage;
import com.bok.krypto.helper.MarketHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MarketConsumer {

    @Autowired
    MarketHelper marketHelper;

    @JmsListener(destination = "${active-mq.market-sell-queue}")
    public void marketListener(SellMessage message) {
        log.info("Received Message: " + message.toString());
        marketHelper.handle(message);
    }

    @JmsListener(destination = "${active-mq.market-purchase-queue}")
    public void marketListener(PurchaseMessage message) {
        log.info("Received Message: " + message.toString());
        marketHelper.handle(message);
    }
}
