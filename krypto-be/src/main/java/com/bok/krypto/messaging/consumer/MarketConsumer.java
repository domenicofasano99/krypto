package com.bok.krypto.messaging.consumer;

import com.bok.krypto.helper.MarketHelper;
import com.bok.krypto.messaging.messages.PurchaseMessage;
import com.bok.krypto.messaging.messages.SellMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MarketConsumer {

    @Autowired
    MarketHelper marketHelper;

    @JmsListener(destination = "${queue.market.sell}")
    public void marketListener(SellMessage message) {
        log.info("Received market sell essage: " + message.toString());
        marketHelper.handle(message);
    }

    @JmsListener(destination = "${queue.market.purchase}")
    public void marketListener(PurchaseMessage message) {
        log.info("Received market purchase message: " + message.toString());
        marketHelper.handle(message);
    }
}
