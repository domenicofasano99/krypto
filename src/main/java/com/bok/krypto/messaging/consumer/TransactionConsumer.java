package com.bok.krypto.messaging.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TransactionConsumer {

    /*
    @JmsListener(destination = "${active-mq.transactions}")
    public void onTransferMessage(TransactionDTO transaction) {
        log.info("Received Message: " + transaction.toString());
    }

     */

}
