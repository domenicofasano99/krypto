package com.bok.krypto.messaging.consumer;

import com.bok.krypto.helper.WalletHelper;
import com.bok.krypto.messaging.messages.WalletMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WalletConsumer {
    @Autowired
    WalletHelper walletHelper;

    @JmsListener(destination = "${active-mq.wallets-queue}")
    public void walletListener(WalletMessage message) {
        log.info("processing : " + message.toString());
        walletHelper.handleMessage(message);

    }
}
