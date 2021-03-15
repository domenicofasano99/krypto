package com.bok.krypto.messaging.consumer;

import com.bok.integration.UserCreationDTO;
import com.bok.krypto.helper.TransferHelper;
import com.bok.krypto.helper.UserHelper;
import com.bok.krypto.messaging.TransferMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TransferConsumer {

    @Autowired
    TransferHelper transferHelper;

    @JmsListener(destination = "${active-mq.transfers-queue}")
    public void onUserCreationMessage(TransferMessage transferMessage) {
        log.info("Received Message: " + transferMessage.toString());
        transferHelper.handle(transferMessage);
    }
}
