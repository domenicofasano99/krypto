package com.bok.krypto.service.interfaces;

import com.bok.bank.integration.message.BankDepositMessage;
import com.bok.bank.integration.message.BankWithdrawalMessage;
import com.bok.krypto.messaging.messages.PurchaseMessage;
import com.bok.krypto.messaging.messages.SellMessage;
import com.bok.krypto.messaging.messages.TransferMessage;
import com.bok.krypto.messaging.messages.WalletCreationMessage;
import com.bok.krypto.messaging.messages.WalletDeleteMessage;
import com.bok.parent.integration.message.EmailMessage;
import org.springframework.stereotype.Service;

@Service
public interface MessageService {
    void sendWallet(WalletCreationMessage walletCreationMessage);

    void sendTransfer(TransferMessage transferMessage);

    void sendEmail(EmailMessage emailWalletCreation);

    void sendPurchase(PurchaseMessage purchaseMessage);

    void sendSell(SellMessage sellMessage);

    void sendBankDeposit(BankDepositMessage bankDepositMessage);

    void sendBankWithdrawal(BankWithdrawalMessage bankWithdrawalMessage);

    void sendWalletDeletion(WalletDeleteMessage message);
}
