package com.bok.krypto.messaging.producer;

import com.bok.bank.integration.message.BankDepositMessage;
import com.bok.bank.integration.message.BankWithdrawalMessage;
import com.bok.parent.integration.message.AccountClosureMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BankProducer {

    @Autowired
    JmsTemplate jmsTemplate;

    @Value("${queue.bank.deposit}")
    private String bankDepositQueue;

    @Value("${queue.bank.withdrawal}")
    private String bankWithdrawalQueue;

    @Value("${queue.bank.account-deletion}")
    private String bankAccountDeletion;

    public void send(BankDepositMessage bankDepositMessage) {
        try {
            log.info("Sending message: {}", bankDepositMessage);
            jmsTemplate.convertAndSend(bankDepositQueue, bankDepositMessage);
        } catch (Exception e) {
            log.error("Received Exception during send Message: ", e);
        }
    }

    public void send(BankWithdrawalMessage bankWithDrawalMessage) {
        try {
            log.info("Sending message: {}", bankWithDrawalMessage);
            jmsTemplate.convertAndSend(bankWithdrawalQueue, bankWithDrawalMessage);
        } catch (Exception e) {
            log.error("Received Exception during send Message: ", e);
        }
    }

    public void send(AccountClosureMessage message) {
        try {
            log.info("Sending message: {}", message);
            jmsTemplate.convertAndSend(bankAccountDeletion, message);
        } catch (Exception e) {
            log.error("Received Exception during send Message: ", e);
        }
    }
}
