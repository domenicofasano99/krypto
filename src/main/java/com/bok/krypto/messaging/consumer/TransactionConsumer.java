package com.bok.krypto.messaging.consumer;

import com.bok.krypto.helper.TransactionHelper;
import com.bok.krypto.messaging.TransactionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TransactionConsumer {

    @Autowired
    TransactionHelper transactionHelper;

    @JmsListener(destination = "${active-mq.transactions-queue}")
    public void onTransactionMessageReceived(TransactionMessage message) {
        log.info("Received Message: " + message.toString());
        transactionHelper.handleMessage(message);

    }
}
