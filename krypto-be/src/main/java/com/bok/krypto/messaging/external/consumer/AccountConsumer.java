package com.bok.krypto.messaging.external.consumer;

import com.bok.krypto.helper.AccountHelper;
import com.bok.parent.integration.message.AccountCreationMessage;
import com.bok.parent.integration.message.AccountDeletionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AccountConsumer {

    @Autowired
    AccountHelper accountHelper;

    @JmsListener(destination = "${active-mq.krypto-users}")
    public void accountCreationListener(AccountCreationMessage message) {
        log.info("Received message from parent: " + message.toString());
        accountHelper.handle(message);
    }

    @JmsListener(destination = "${active-mq.krypto-account-deletion}")
    public void accountDeletionListener(AccountDeletionMessage message) {
        log.info("Received message from parent: " + message.toString());
        accountHelper.handle(message);
    }


}
