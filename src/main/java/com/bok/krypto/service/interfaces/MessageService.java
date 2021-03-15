package com.bok.krypto.service.interfaces;

import com.bok.integration.EmailMessage;
import com.bok.krypto.messaging.TransactionMessage;
import com.bok.krypto.messaging.TransferMessage;
import com.bok.krypto.messaging.WalletMessage;
import org.springframework.stereotype.Service;

@Service
public interface MessageService {
    public void send(WalletMessage walletMessage);

    public void send(TransactionMessage transactionMessage);

    public void send(TransferMessage transferMessage);

    public void send(EmailMessage emailWalletCreation);
}
