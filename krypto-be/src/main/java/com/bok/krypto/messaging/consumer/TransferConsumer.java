package com.bok.krypto.messaging.consumer;

import com.bok.krypto.helper.TransferHelper;
import com.bok.krypto.messaging.messages.TransferMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TransferConsumer {

    @Autowired
    TransferHelper transferHelper;

    @JmsListener(destination = "${queue.transfers}")
    public void transferListener(TransferMessage transferMessage) {
        log.info("Received transfer message: " + transferMessage.toString());
        transferHelper.handle(transferMessage);
    }
}
