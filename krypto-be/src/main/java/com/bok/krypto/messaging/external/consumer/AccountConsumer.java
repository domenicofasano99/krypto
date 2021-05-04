package com.bok.krypto.messaging.external.consumer;

import com.bok.integration.AccountCreationMessage;
import com.bok.krypto.helper.AccountHelper;
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
    public void userListener(AccountCreationMessage message) {
        log.info("Received Message: " + message.toString());
        accountHelper.handle(message);
    }


}
