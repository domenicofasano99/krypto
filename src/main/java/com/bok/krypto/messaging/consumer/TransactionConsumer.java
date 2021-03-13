package com.bok.krypto.messaging.consumer;

import com.bok.krypto.dto.TransactionDTO;
import com.bok.krypto.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TransactionConsumer {

    @JmsListener(destination = "${active-mq.transactions}")
    public void onTransferMessage(TransactionDTO transaction) {
        log.info("Received Message: " + transaction.toString());
    }

}
