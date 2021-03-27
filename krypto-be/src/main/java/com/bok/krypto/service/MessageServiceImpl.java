package com.bok.krypto.service;

import com.bok.integration.EmailMessage;
import com.bok.krypto.messaging.messages.*;
import com.bok.krypto.messaging.producer.*;
import com.bok.krypto.service.interfaces.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    WalletProducer walletProducer;

    @Autowired
    TransactionProducer transactionProducer;

    @Autowired
    TransferProducer transferProducer;

    @Autowired
    EmailProducer emailProducer;

    @Autowired
    MarketProducer marketProducer;

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
        emailProducer.send(emailMessage);
    }

    @Override
    public void send(PurchaseMessage purchaseMessage) {
        marketProducer.send(purchaseMessage);
    }

    @Override
    public void send(SellMessage sellMessage) {
        marketProducer.send(sellMessage);
    }
}
