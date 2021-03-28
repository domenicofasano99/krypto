package com.bok.krypto.messaging.consumer;

import com.bok.integration.UserCreationDTO;
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
    public void userListener(UserCreationDTO userCreationDTO) {
        log.info("Received Message: " + userCreationDTO.toString());
        accountHelper.save(userCreationDTO.id);
    }


}
