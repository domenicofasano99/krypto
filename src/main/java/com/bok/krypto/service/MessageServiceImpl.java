package com.bok.krypto.service;

import com.bok.integration.EmailMessage;
import com.bok.krypto.messaging.TransactionMessage;
import com.bok.krypto.messaging.TransferMessage;
import com.bok.krypto.messaging.WalletMessage;
import com.bok.krypto.messaging.producer.EmailProducer;
import com.bok.krypto.messaging.producer.TransactionProducer;
import com.bok.krypto.messaging.producer.TransferProducer;
import com.bok.krypto.messaging.producer.WalletProducer;
import com.bok.krypto.service.interfaces.MessageService;
import org.springframework.beans.factory.annotation.Autowired;

public class MessageServiceImpl implements MessageService {
    @Autowired
    WalletProducer walletProducer;

    @Autowired
    TransactionProducer transactionProducer;

    @Autowired
    TransferProducer transferProducer;

    @Autowired
    EmailProducer emailProducer;

    @Override
    public void send(TransactionMessage transactionMessage) {
        transactionProducer.send(transactionMessage);
    }

    @Override
    public void send(TransferMessage transferMessage) {
        transferProducer.send(transferMessage);
    }

    @Override
    public void send(WalletMessage walletMessage) {
        walletProducer.send(walletMessage);
    }

    @Override
    public void send(EmailMessage emailMessage) {

    }
}
