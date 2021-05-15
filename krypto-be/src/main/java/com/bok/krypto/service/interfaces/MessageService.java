package com.bok.krypto.service.interfaces;

import com.bok.bank.integration.message.BankDepositMessage;
import com.bok.bank.integration.message.BankWithdrawalMessage;
import com.bok.krypto.messaging.internal.messages.PurchaseMessage;
import com.bok.krypto.messaging.internal.messages.SellMessage;
import com.bok.krypto.messaging.internal.messages.TransferMessage;
import com.bok.krypto.messaging.internal.messages.WalletMessage;
import com.bok.parent.integration.message.EmailMessage;
import org.springframework.stereotype.Service;

@Service
public interface MessageService {
    public void sendWallet(WalletMessage walletMessage);

    public void sendTransfer(TransferMessage transferMessage);

    public void sendEmail(EmailMessage emailWalletCreation);

    public void sendPurchase(PurchaseMessage purchaseMessage);

    public void sendSell(SellMessage sellMessage);

    public void sendBankDeposit(BankDepositMessage bankDepositMessage);

    public void sendBankWithdrawal(BankWithdrawalMessage bankWithdrawalMessage);
}
