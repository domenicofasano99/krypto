package com.bok.krypto.service;

import com.bok.integration.EmailMessage;
import com.bok.krypto.communication.messages.PurchaseMessage;
import com.bok.krypto.communication.messages.SellMessage;
import com.bok.krypto.communication.messages.TransactionMessage;
import com.bok.krypto.communication.messages.TransferMessage;
import com.bok.krypto.communication.messages.WalletMessage;
import com.bok.krypto.communication.producer.EmailProducer;
import com.bok.krypto.communication.producer.MarketProducer;
import com.bok.krypto.communication.producer.TransactionProducer;
import com.bok.krypto.communication.producer.TransferProducer;
import com.bok.krypto.communication.producer.WalletProducer;
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
    public void sendTransfer(TransferMessage transferMessage) {
        transferProducer.send(transferMessage);
    }

    @Override
    public void send(WalletMessage walletMessage) {
        walletProducer.send(walletMessage);
    }

    @Override
    public void sendEmail(EmailMessage emailMessage) {
        emailProducer.send(emailMessage);
    }

    @Override
    public void sendPurchase(PurchaseMessage purchaseMessage) {
        marketProducer.send(purchaseMessage);
    }

    @Override
    public void send(SellMessage sellMessage) {
        marketProducer.send(sellMessage);
    }
}
