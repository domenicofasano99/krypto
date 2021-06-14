package com.bok.krypto.messaging.internal.consumer;

import com.bok.krypto.helper.AccountHelper;
import com.bok.parent.integration.message.AccountCreationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AccountConsumer {

    @Autowired
    AccountHelper accountHelper;

    @JmsListener(destination = "${queues.krypto-users}")
    public void marketListener(AccountCreationMessage message) {
        log.info("Received Account Creation Message: " + message.toString());
        accountHelper.handle(message);
    }
}
