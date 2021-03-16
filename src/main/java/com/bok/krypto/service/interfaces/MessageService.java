package com.bok.krypto.service.interfaces;

import com.bok.integration.EmailMessage;
import com.bok.krypto.messaging.messages.MarketMessage;
import com.bok.krypto.messaging.messages.TransactionMessage;
import com.bok.krypto.messaging.messages.TransferMessage;
import com.bok.krypto.messaging.messages.WalletAbstractMessage;
import org.springframework.stereotype.Service;

@Service
public interface MessageService {
    public void send(WalletAbstractMessage walletMessage);

    public void send(TransactionMessage transactionMessage);

    public void send(TransferMessage transferMessage);

    public void send(EmailMessage emailWalletCreation);

    public void send(MarketMessage message);
}
