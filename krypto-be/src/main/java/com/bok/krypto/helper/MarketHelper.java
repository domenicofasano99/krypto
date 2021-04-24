package com.bok.krypto.helper;

import com.bok.integration.EmailMessage;
import com.bok.integration.krypto.PurchaseRequestDTO;
import com.bok.integration.krypto.dto.SellRequestDTO;
import com.bok.integration.krypto.dto.TransactionDTO;
import com.bok.krypto.communication.messages.PurchaseMessage;
import com.bok.krypto.communication.messages.SellMessage;
import com.bok.krypto.exception.ErrorCodes;
import com.bok.krypto.exception.InsufficientBalanceException;
import com.bok.krypto.model.Account;
import com.bok.krypto.model.Krypto;
import com.bok.krypto.model.Transaction;
import com.bok.krypto.model.Wallet;
import com.bok.krypto.service.bank.BankService;
import com.bok.krypto.service.interfaces.MessageService;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class MarketHelper {

    @Autowired
    WalletHelper walletHelper;

    @Autowired
    KryptoHelper kryptoHelper;

    @Autowired
    TransactionHelper transactionHelper;

    @Autowired
    AccountHelper accountHelper;

    @Autowired
    MessageService messageService;

    @Autowired
    BankService bankService;


    public TransactionDTO buy(Long userId, PurchaseRequestDTO purchaseRequestDTO) {
        Preconditions.checkNotNull(purchaseRequestDTO);
        Preconditions.checkNotNull(purchaseRequestDTO.symbol);
        Preconditions.checkNotNull(purchaseRequestDTO.amount);
        Preconditions.checkNotNull(purchaseRequestDTO.amount);
        Preconditions.checkArgument(accountHelper.existsById(userId), ErrorCodes.USER_DOES_NOT_EXIST);
        Preconditions.checkArgument(kryptoHelper.existsBySymbol(purchaseRequestDTO.symbol), ErrorCodes.KRYPTO_DOES_NOT_EXIST);
        Preconditions.checkArgument(purchaseRequestDTO.amount.compareTo(BigDecimal.ZERO) > 0, ErrorCodes.NEGATIVE_AMOUNT_GIVEN);
        Preconditions.checkArgument(bankService.getUserBalance(userId).balance.compareTo(BigDecimal.ZERO) > 0);
        Transaction t = new Transaction(Transaction.Type.BUY);
        t = transactionHelper.saveOrUpdate(t);
        PurchaseMessage message = new PurchaseMessage();
        message.userId = userId;
        message.transactionId = t.getId();
        message.amount = purchaseRequestDTO.amount;
        message.symbol = purchaseRequestDTO.symbol;
        messageService.send(message);
        return new TransactionDTO(t.getId(), t.status.name(), t.getType().name(), purchaseRequestDTO.amount);
    }

    public void handle(PurchaseMessage purchaseMessage) {
        log.info("Processing purchase {}", purchaseMessage);
        Transaction t = transactionHelper.findById(purchaseMessage.transactionId);
        Wallet destination = walletHelper.findByUserIdAndSymbol(purchaseMessage.userId, purchaseMessage.symbol);
        Account account = accountHelper.findById(purchaseMessage.userId);
        t.setWallet(destination);
        t.setAmount(purchaseMessage.amount);
        t.setUser(account);

        try {
            walletHelper.deposit(destination, purchaseMessage.amount);
        } catch (InsufficientBalanceException ex) {
            EmailMessage email = new EmailMessage();
            email.subject = "Insufficient Balance in your account";
            email.to = account.getEmail();
            email.text = "Your PURCHASE transaction of " + purchaseMessage.amount + " " + purchaseMessage.symbol + " has been DECLINED due to insufficient balance.";
            t.setStatus(Transaction.Status.REJECTED);
            messageService.send(email);
        }
        log.info("Completed purchase ID:{} for {} of {} {}", purchaseMessage.transactionId, purchaseMessage.userId, purchaseMessage.symbol, purchaseMessage.amount);
        EmailMessage email = new EmailMessage();
        email.subject = "Transfer executed";
        email.to = account.getEmail();
        email.text = "Your PURCHASE of " + purchaseMessage.amount + " " + purchaseMessage.symbol + " has been ACCEPTED.";
        t.setStatus(Transaction.Status.SETTLED);
        transactionHelper.saveOrUpdate(t);
        messageService.send(email);
    }

    public TransactionDTO sell(Long userId, SellRequestDTO sellRequestDTO) {
        Preconditions.checkNotNull(sellRequestDTO);
        Preconditions.checkNotNull(sellRequestDTO.userId);
        Preconditions.checkNotNull(sellRequestDTO.symbol);
        Preconditions.checkNotNull(sellRequestDTO.amount);
        Preconditions.checkArgument(accountHelper.existsById(userId), ErrorCodes.USER_DOES_NOT_EXIST);
        Preconditions.checkArgument(kryptoHelper.existsBySymbol(sellRequestDTO.symbol), ErrorCodes.KRYPTO_DOES_NOT_EXIST);
        Preconditions.checkArgument(sellRequestDTO.amount.compareTo(BigDecimal.ZERO) <= 0, "Cannot SELL a negative amount.");


        Transaction t = new Transaction(Transaction.Type.SELL);
        t = transactionHelper.saveOrUpdate(t);
        SellMessage message = new SellMessage();
        message.userId = userId;
        message.transactionId = t.getId();
        message.amount = sellRequestDTO.amount;
        message.symbol = sellRequestDTO.symbol;
        messageService.send(message);
        return new TransactionDTO(t.getId(), t.status.name(), t.getType().name(), sellRequestDTO.amount);


    }

    public void handle(SellMessage sellMessage) {
        log.info("Processing sell {}", sellMessage);
        Transaction t = transactionHelper.findById(sellMessage.transactionId);
        Wallet source = walletHelper.findByUserIdAndSymbol(sellMessage.userId, sellMessage.symbol);
        Account account = accountHelper.findById(sellMessage.userId);
        t.setWallet(source);
        t.setAmount(sellMessage.amount);
        t.setUser(account);
        String subject, to, text;
        try {
            walletHelper.withdraw(source, sellMessage.amount);
        } catch (InsufficientBalanceException ex) {
            subject = "Insufficient Balance in your account";
            to = account.getEmail();
            text = "Your SELL transaction of " + sellMessage.amount + " " + sellMessage.symbol + " has been DECLINED due to insufficient balance.";
            t.setStatus(Transaction.Status.REJECTED);
            sendMarketEmail(subject, to, text);
        }
        subject = "Transfer executed";
        to = account.getEmail();
        text = "Your SELL of " + sellMessage.amount + " " + sellMessage.symbol + " has been ACCEPTED.";
        t.setStatus(Transaction.Status.SETTLED);
        transactionHelper.saveOrUpdate(t);
        sendMarketEmail(subject, to, text);
    }

    public Transaction emptyWallet(Account account, Wallet walletToEmpty) {

        BigDecimal amountToSell = walletToEmpty.getAvailableAmount();
        Transaction sell = new Transaction();
        sell.setAmount(amountToSell);
        sell.setUser(account);
        BigDecimal netWorth = convert(walletToEmpty.getKrypto(), amountToSell);
        //send message to bank to credit netWorth USD

        transactionHelper.saveOrUpdate(sell);

        String subject, to, text;
        subject = "Wallet emptied";
        to = account.getEmail();
        text = "Your " + walletToEmpty.getKrypto().getSymbol() + " wallet has been emptied, you should receive " +
                "the converted amount in your bank account in a few minutes.";
        sendMarketEmail(subject, to, text);
        return sell;

    }


    public BigDecimal convert(Krypto k, BigDecimal amount) {
        return k.getPrice().multiply(amount);
    }

    public void sendMarketEmail(String subject, String email, String text) {
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.subject = subject;
        emailMessage.to = email;
        emailMessage.text = text;
        messageService.send(emailMessage);
    }
}
