package com.bok.krypto.service.interfaces;

import com.bok.integration.EmailMessage;
import com.bok.krypto.communication.messages.PurchaseMessage;
import com.bok.krypto.communication.messages.SellMessage;
import com.bok.krypto.communication.messages.TransferMessage;
import com.bok.krypto.communication.messages.WalletMessage;
import org.springframework.stereotype.Service;

@Service
public interface MessageService {
    public void sendWallet(WalletMessage walletMessage);

    public void sendTransfer(TransferMessage transferMessage);

    public void sendEmail(EmailMessage emailWalletCreation);

    public void sendPurchase(PurchaseMessage purchaseMessage);

    public void sendSell(SellMessage sellMessage);
}
