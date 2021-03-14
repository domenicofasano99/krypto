package com.bok.krypto.messaging.consumer;

import com.bok.integration.UserCreationDTO;
import com.bok.krypto.helper.UserHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageConsumer {

    @Autowired
    UserHelper userHelper;

    @JmsListener(destination = "${active-mq.users-queue}")
    public void onUserCreationMessage(UserCreationDTO userCreationDTO) {
        log.info("Received Message: " + userCreationDTO.toString());

        userHelper.save(userCreationDTO.id);

    }


}
