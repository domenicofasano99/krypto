package com.bok.krypto.consumer;

import com.bok.krypto.helper.TransactionHelper;
import com.bok.krypto.communication.messages.TransactionMessage;
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
    public void transactionListener(TransactionMessage message) {
        log.info("Received Message: " + message.toString());
        transactionHelper.handleMessage(message);

    }
}
