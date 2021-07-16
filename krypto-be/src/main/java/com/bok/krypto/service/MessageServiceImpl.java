package com.bok.krypto.service;

import com.bok.bank.integration.message.BankDepositMessage;
import com.bok.bank.integration.message.BankWithdrawalMessage;
import com.bok.krypto.messaging.messages.PurchaseMessage;
import com.bok.krypto.messaging.messages.SellMessage;
import com.bok.krypto.messaging.messages.TransferMessage;
import com.bok.krypto.messaging.messages.WalletCreationMessage;
import com.bok.krypto.messaging.messages.WalletDeleteMessage;
import com.bok.krypto.messaging.producer.BankProducer;
import com.bok.krypto.messaging.producer.EmailProducer;
import com.bok.krypto.messaging.producer.MarketProducer;
import com.bok.krypto.messaging.producer.TransferProducer;
import com.bok.krypto.messaging.producer.WalletProducer;
import com.bok.krypto.service.interfaces.MessageService;
import com.bok.parent.integration.message.EmailMessage;
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

    @Autowired
    BankProducer bankProducer;

    @Override
    public void sendTransfer(TransferMessage transferMessage) {
        transferProducer.send(transferMessage);
    }

    @Override
    public void sendWallet(WalletCreationMessage walletCreationMessage) {
        walletProducer.send(walletCreationMessage);
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

    @Override
    public void sendBankDeposit(BankDepositMessage bankDepositMessage) {
        bankProducer.send(bankDepositMessage);
    }

    @Override
    public void sendBankWithdrawal(BankWithdrawalMessage bankWithdrawalMessage) {
        bankProducer.send(bankWithdrawalMessage);
    }

    @Override
    public void sendWalletDeletion(WalletDeleteMessage message) {
        walletProducer.send(message);
    }
}
