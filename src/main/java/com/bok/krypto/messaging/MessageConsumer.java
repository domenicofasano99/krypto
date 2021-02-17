package com.bok.krypto.messaging;

import com.bok.krypto.model.Transfer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

@Component
@Slf4j
public class MessageConsumer implements MessageListener {


    @Override
    @JmsListener(destination = "${active-mq.transfers-topic}")
    public void onMessage(Message message) {
        try{
            ObjectMessage objectMessage = (ObjectMessage)message;
            Transfer transfer = (Transfer)objectMessage.getObject();
            //do additional processing
            log.info("Received Message: "+ transfer.toString());
        } catch(Exception e) {
            log.error("Received Exception : "+ e);
        }

    }
}
