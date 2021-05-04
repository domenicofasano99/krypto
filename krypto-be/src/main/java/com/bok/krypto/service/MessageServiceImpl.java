package com.bok.krypto.service;

import com.bok.integration.EmailMessage;
import com.bok.krypto.messaging.internal.messages.PurchaseMessage;
import com.bok.krypto.messaging.internal.messages.SellMessage;
import com.bok.krypto.messaging.internal.messages.TransferMessage;
import com.bok.krypto.messaging.internal.messages.WalletMessage;
import com.bok.krypto.messaging.internal.producer.EmailProducer;
import com.bok.krypto.messaging.internal.producer.MarketProducer;
import com.bok.krypto.messaging.internal.producer.TransferProducer;
import com.bok.krypto.messaging.internal.producer.WalletProducer;
import com.bok.krypto.service.interfaces.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    WalletProducer walletProducer;

    @Autowired
    TransferProducer transferProducer;

    @Autowired
    EmailProducer emailProducer;

    @Autowired
    MarketProducer marketProducer;

    @Override
    public void sendTransfer(TransferMessage transferMessage) {
        transferProducer.send(transferMessage);
    }

    @Override
    public void sendWallet(WalletMessage walletMessage) {
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
    public void sendSell(SellMessage sellMessage) {
        marketProducer.send(sellMessage);
    }
}
