package com.bok.krypto.helper;

import com.bok.integration.EmailMessage;
import com.bok.integration.krypto.PurchaseRequestDTO;
import com.bok.integration.krypto.dto.SellRequestDTO;
import com.bok.integration.krypto.dto.TransactionDTO;
import com.bok.krypto.exception.ErrorCodes;
import com.bok.krypto.exception.InsufficientBalanceException;
import com.bok.krypto.messaging.internal.messages.PurchaseMessage;
import com.bok.krypto.messaging.internal.messages.SellMessage;
import com.bok.krypto.model.Account;
import com.bok.krypto.model.Activity;
import com.bok.krypto.model.Krypto;
import com.bok.krypto.model.Transaction;
import com.bok.krypto.model.Wallet;
import com.bok.krypto.service.bank.BankService;
import com.bok.krypto.service.interfaces.MessageService;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
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


    public TransactionDTO buy(Long accountId, PurchaseRequestDTO purchaseRequestDTO) {
        Preconditions.checkNotNull(purchaseRequestDTO);
        Preconditions.checkNotNull(purchaseRequestDTO.symbol);
        Preconditions.checkNotNull(purchaseRequestDTO.amount);
        Preconditions.checkNotNull(purchaseRequestDTO.amount);
        Preconditions.checkArgument(accountHelper.existsById(accountId), ErrorCodes.USER_DOES_NOT_EXIST);
        Preconditions.checkArgument(kryptoHelper.existsBySymbol(purchaseRequestDTO.symbol), ErrorCodes.KRYPTO_DOES_NOT_EXIST);
        Preconditions.checkArgument(purchaseRequestDTO.amount.compareTo(BigDecimal.ZERO) > 0, ErrorCodes.NEGATIVE_AMOUNT_GIVEN);
        Preconditions.checkArgument(bankService.getAccountBalance(accountId).balance.compareTo(convertIntoUSD(purchaseRequestDTO.symbol, purchaseRequestDTO.amount)) >= 0, "Insufficient balance");
        Account account = accountHelper.findById(accountId);

        Transaction transaction = new Transaction(Transaction.Type.PURCHASE);
        transaction = transactionHelper.saveOrUpdate(transaction);

        PurchaseMessage message = new PurchaseMessage();
        message.accountId = account.getId();
        message.transactionId = transaction.getId();
        message.amount = purchaseRequestDTO.amount;
        message.symbol = purchaseRequestDTO.symbol;
        messageService.sendPurchase(message);
        return new TransactionDTO(transaction.getPublicId(), transaction.status.name(), transaction.getType().name(), purchaseRequestDTO.amount);
    }

    public void handle(PurchaseMessage purchaseMessage) {
        log.info("Processing purchase {}", purchaseMessage);
        Transaction transaction = transactionHelper.findById(purchaseMessage.transactionId);
        Wallet destination = walletHelper.findByAccountIdAndSymbol(purchaseMessage.accountId, purchaseMessage.symbol);
        Account account = accountHelper.findById(purchaseMessage.accountId);
        transaction.setWallet(destination);
        transaction.setAmount(purchaseMessage.amount);
        transaction.setAccount(account);

        try {
            walletHelper.deposit(account, destination, purchaseMessage.amount);
        } catch (InsufficientBalanceException ex) {
            log.info("Purchase {} error, insufficient balance", purchaseMessage);
            EmailMessage email = new EmailMessage();
            email.subject = "Insufficient Balance in your account";
            email.to = account.getEmail();
            email.text = "Your PURCHASE transaction of " + purchaseMessage.amount + " " + purchaseMessage.symbol + " has been DECLINED due to insufficient balance.";
            transaction.setStatus(Activity.Status.REJECTED);
            messageService.sendEmail(email);
        }
        log.info("Completed purchase ID:{} for {} of {} {}", purchaseMessage.transactionId, purchaseMessage.accountId, purchaseMessage.symbol, purchaseMessage.amount);
        EmailMessage email = new EmailMessage();
        email.subject = "Transfer executed";
        email.to = account.getEmail();
        email.text = "Your PURCHASE of " + purchaseMessage.amount + " " + purchaseMessage.symbol + " has been ACCEPTED.";
        transaction.setStatus(Activity.Status.SETTLED);
        transactionHelper.saveOrUpdate(transaction);
        messageService.sendEmail(email);
    }

    @Transactional
    public TransactionDTO sell(Long userId, SellRequestDTO sellRequestDTO) {
        Preconditions.checkNotNull(sellRequestDTO);
        Preconditions.checkNotNull(sellRequestDTO.userId);
        Preconditions.checkNotNull(sellRequestDTO.symbol);
        Preconditions.checkNotNull(sellRequestDTO.amount);
        Preconditions.checkArgument(accountHelper.existsById(userId), ErrorCodes.USER_DOES_NOT_EXIST);
        Preconditions.checkArgument(kryptoHelper.existsBySymbol(sellRequestDTO.symbol), ErrorCodes.KRYPTO_DOES_NOT_EXIST);
        Preconditions.checkArgument(sellRequestDTO.amount.compareTo(BigDecimal.ZERO) <= 0, "Cannot SELL a negative amount.");


        Transaction transaction = new Transaction(Transaction.Type.PURCHASE);
        transaction = transactionHelper.saveOrUpdate(transaction);
        SellMessage message = new SellMessage();
        message.accountId = userId;
        message.transactionId = transaction.getId();
        message.amount = sellRequestDTO.amount;
        message.symbol = sellRequestDTO.symbol;
        messageService.sendSell(message);
        return new TransactionDTO(transaction.getPublicId(), transaction.status.name(), transaction.getType().name(), sellRequestDTO.amount);


    }

    @Transactional
    public void handle(SellMessage sellMessage) {
        log.info("Processing sell {}", sellMessage);
        Transaction transaction = transactionHelper.findById(sellMessage.transactionId);
        Wallet source = walletHelper.findByAccountIdAndSymbol(sellMessage.accountId, sellMessage.symbol);
        Account account = accountHelper.findById(sellMessage.accountId);
        transaction.setWallet(source);
        transaction.setAmount(sellMessage.amount);
        transaction.setAccount(account);

        String subject, to, text;
        try {
            walletHelper.withdraw(source, sellMessage.amount);
        } catch (InsufficientBalanceException ex) {
            subject = "Insufficient Balance in your account";
            to = account.getEmail();
            text = "Your SELL transaction of " + sellMessage.amount + " " + sellMessage.symbol + " has been DECLINED due to insufficient balance.";
            transaction.setStatus(Activity.Status.REJECTED);
            sendMarketEmail(subject, to, text);
        }
        subject = "Transfer executed";
        to = account.getEmail();
        text = "Your SELL of " + sellMessage.amount + " " + sellMessage.symbol + " has been ACCEPTED.";
        transaction.setStatus(Activity.Status.SETTLED);
        transactionHelper.saveOrUpdate(transaction);
        sendMarketEmail(subject, to, text);
    }

    @Transactional
    public Transaction emptyWallet(Account account, Wallet walletToEmpty) {

        BigDecimal amountToSell = walletToEmpty.getAvailableAmount();
        Transaction sell = new Transaction();
        sell.setAmount(amountToSell);
        sell.setAccount(account);
        BigDecimal netWorth = convertIntoUSD(walletToEmpty.getKrypto(), amountToSell);
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

    public BigDecimal convertIntoUSD(String symbol, BigDecimal amount) {
        return convertIntoUSD(kryptoHelper.findBySymbol(symbol), amount);
    }


    public BigDecimal convertIntoUSD(Krypto k, BigDecimal amount) {
        return k.getPrice().multiply(amount);
    }

    public void sendMarketEmail(String subject, String email, String text) {
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.subject = subject;
        emailMessage.to = email;
        emailMessage.text = text;
        messageService.sendEmail(emailMessage);
    }
}
