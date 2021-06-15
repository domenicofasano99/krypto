package com.bok.krypto.messaging.consumer;

import com.bok.krypto.helper.WalletHelper;
import com.bok.krypto.messaging.messages.WalletCreationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WalletConsumer {
    @Autowired
    WalletHelper walletHelper;

    @JmsListener(destination = "${queue.wallet.creation}")
    public void walletListener(WalletCreationMessage message) {
        log.info("Received wallet message : " + message.toString());
        walletHelper.handleMessage(message);
    }
}
