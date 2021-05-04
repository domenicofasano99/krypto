package com.bok.krypto.consumer;

import com.bok.krypto.communication.messages.PurchaseMessage;
import com.bok.krypto.communication.messages.SellMessage;
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

    @JmsListener(destination = "${active-mq.market-sell}")
    public void marketListener(SellMessage message) {
        log.info("Received Message: " + message.toString());
        marketHelper.handle(message);
    }

    @JmsListener(destination = "${active-mq.market-purchase}")
    public void marketListener(PurchaseMessage message) {
        log.info("Received Message: " + message.toString());
        marketHelper.handle(message);
    }
}
