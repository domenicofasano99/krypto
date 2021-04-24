package com.bok.krypto.consumer;

import com.bok.krypto.helper.AccountHelper;
import com.bok.parent.message.KryptoAccountCreationMessage;
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
    public void userListener(KryptoAccountCreationMessage message) {
        log.info("Received Message: " + message.toString());
        accountHelper.saveOrUpdate(message);
    }


}
