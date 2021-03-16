package com.bok.krypto.messaging.consumer;

import com.bok.krypto.helper.WalletHelper;
import com.bok.krypto.messaging.messages.WalletAbstractMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WalletConsumer {
    @Autowired
    WalletHelper walletHelper;

    @JmsListener(destination = "${active-mq.transactions-queue}")
    public void onTransactionMessageReceived(WalletAbstractMessage message) {
        log.info("Received Message: " + message.toString());
        walletHelper.handleMessage(message);

    }
}
