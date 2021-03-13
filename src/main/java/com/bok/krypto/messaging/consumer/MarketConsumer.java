package com.bok.krypto.messaging.consumer;

import com.bok.krypto.dto.TransactionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MarketConsumer {

    @JmsListener(destination = "${active-mq.market}")
    public void onTransferMessage(TransactionDTO transaction) {
        log.info("Received Message: " + transaction.toString());
    }
}
