package com.bok.krypto.helper;

import com.bok.bank.integration.dto.AuthorizationResponseDTO;
import com.bok.bank.integration.message.BankDepositMessage;
import com.bok.bank.integration.message.BankWithdrawalMessage;
import com.bok.bank.integration.util.Money;
import com.bok.krypto.exception.ErrorCodes;
import com.bok.krypto.exception.TransactionException;
import com.bok.krypto.integration.internal.dto.PurchaseRequestDTO;
import com.bok.krypto.integration.internal.dto.SellRequestDTO;
import com.bok.krypto.integration.internal.dto.TransactionDTO;
import com.bok.krypto.messaging.internal.messages.PurchaseMessage;
import com.bok.krypto.messaging.internal.messages.SellMessage;
import com.bok.krypto.model.Account;
import com.bok.krypto.model.Activity;
import com.bok.krypto.model.Krypto;
import com.bok.krypto.model.Transaction;
import com.bok.krypto.model.Wallet;
import com.bok.krypto.service.bank.BankService;
import com.bok.krypto.service.interfaces.MessageService;
import com.bok.parent.integration.message.EmailMessage;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Currency;

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


    public TransactionDTO buy(Long accountId, PurchaseRequestDTO purchaseRequest) {
        Preconditions.checkArgument(accountHelper.existsById(accountId), ErrorCodes.USER_DOES_NOT_EXIST);
        Preconditions.checkArgument(kryptoHelper.existsBySymbol(purchaseRequest.symbol), ErrorCodes.KRYPTO_DOES_NOT_EXIST);
        Preconditions.checkArgument(purchaseRequest.amount.compareTo(BigDecimal.ZERO) > 0, ErrorCodes.NEGATIVE_AMOUNT_GIVEN);

        Krypto k = kryptoHelper.findBySymbol(purchaseRequest.symbol);
        Money money = convertIntoMoney(k, purchaseRequest.amount);

        Transaction transaction = new Transaction(Transaction.Type.PURCHASE);
        transaction = transactionHelper.saveOrUpdate(transaction);

        AuthorizationResponseDTO authorizationResponse = bankService.authorize(accountId, transaction.getPublicId(), money, k.getSymbol());
        transaction.status = authorizationResponse.authorized ? Activity.Status.AUTHORIZED : Activity.Status.DECLINED;
        log.info("Received {} from Bank for transaction {}", transaction.status.name(), transaction.getId());
        transaction.setPublicId(authorizationResponse.extTransactionId);
        transactionHelper.saveOrUpdate(transaction);


        if (authorizationResponse.authorized) {
            sendPurchase(accountId, transaction.getId(), purchaseRequest.amount, purchaseRequest.symbol);
        }
        return new TransactionDTO(transaction.getPublicId(), accountId, Transaction.Type.PURCHASE.name(), purchaseRequest.amount, transaction.status.name());
    }

    @Transactional
    public TransactionDTO sell(Long accountId, SellRequestDTO sellRequest) {
        Preconditions.checkArgument(accountHelper.existsById(accountId), ErrorCodes.USER_DOES_NOT_EXIST);
        Preconditions.checkArgument(kryptoHelper.existsBySymbol(sellRequest.symbol), ErrorCodes.KRYPTO_DOES_NOT_EXIST);
        Preconditions.checkArgument(sellRequest.amount.compareTo(BigDecimal.ZERO) <= 0, "Cannot SELL a negative amount.");


        Transaction transaction = new Transaction(Transaction.Type.PURCHASE);
        transaction = transactionHelper.saveOrUpdate(transaction);

        sendSell(accountId, transaction.getId(), sellRequest.amount, sellRequest.symbol);
        return new TransactionDTO(transaction.getPublicId(), accountId, Transaction.Type.PURCHASE.name(), sellRequest.amount, transaction.status.name());
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
            walletHelper.deposit(destination, purchaseMessage.amount);

            Money amountToWithdraw = convertIntoMoney(destination.getKrypto(), purchaseMessage.amount);
            BankWithdrawalMessage bankWithdrawalMessage = new BankWithdrawalMessage(amountToWithdraw, purchaseMessage.accountId, destination.getKrypto().getSymbol(), transaction.getPublicId());
            bankService.sendBankWithdrawal(bankWithdrawalMessage);

        } catch (TransactionException ex) {
            log.info("Purchase {} error, insufficient balance", purchaseMessage);
            EmailMessage email = new EmailMessage();
            email.subject = "Insufficient Balance in your account";
            email.to = account.getEmail();
            email.text = "Your PURCHASE transaction of " + purchaseMessage.amount + " " + purchaseMessage.symbol + " has been DECLINED due to insufficient balance.";
            transaction.setStatus(Activity.Status.DECLINED);
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

            Money amountToDeposit = convertIntoMoney(source.getKrypto(), sellMessage.amount);
            BankDepositMessage bankDepositMessage = new BankDepositMessage(amountToDeposit, sellMessage.accountId, source.getKrypto().getSymbol());
            bankService.sendBankDeposit(bankDepositMessage);

        } catch (TransactionException ex) {
            subject = "Insufficient Balance in your account";
            to = account.getEmail();
            text = "Your SELL transaction of " + sellMessage.amount + " " + sellMessage.symbol + " has been DECLINED due to insufficient balance.";
            transaction.setStatus(Activity.Status.DECLINED);
            sendMarketEmail(subject, to, text);
        }
        subject = "Sell executed";
        to = account.getEmail();
        text = "Your SELL of " + sellMessage.amount + " " + sellMessage.symbol + " has been ACCEPTED.";
        transaction.setStatus(Activity.Status.SETTLED);
        transactionHelper.saveOrUpdate(transaction);
        sendMarketEmail(subject, to, text);
    }

    @Transactional
    public Transaction emptyWallet(Account account, Wallet walletToEmpty) {

        BigDecimal amountToSell = walletToEmpty.getAvailableAmount();
        Transaction sellTransaction = new Transaction();
        sellTransaction.setAmount(amountToSell);
        sellTransaction.setAccount(account);
        Money money = convertIntoMoney(walletToEmpty.getKrypto(), amountToSell);
        //send message to bank to credit money USD
        bankService.sendBankDeposit(new BankDepositMessage(money, account.getId(), walletToEmpty.getKrypto().getSymbol()));
        transactionHelper.saveOrUpdate(sellTransaction);

        String subject, to, text;
        subject = "Wallet emptied";
        to = account.getEmail();
        text = "Your " + walletToEmpty.getKrypto().getSymbol() + " wallet has been emptied, you should receive " +
                "the converted amount in your bank account in a few minutes.";
        sendMarketEmail(subject, to, text);
        return sellTransaction;

    }

    public Money convertIntoMoney(Krypto k, BigDecimal amount) {
        return new Money(Currency.getInstance("USD"), k.getPrice().multiply(amount));
    }

    public void sendMarketEmail(String subject, String email, String text) {
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.subject = subject;
        emailMessage.to = email;
        emailMessage.text = text;
        messageService.sendEmail(emailMessage);
    }

    private void sendPurchase(Long accountId, Long transactionId, BigDecimal amount, String symbol) {
        PurchaseMessage message = new PurchaseMessage();
        message.accountId = accountId;
        message.transactionId = transactionId;
        message.amount = amount;
        message.symbol = symbol;
        messageService.sendPurchase(message);
    }

    private void sendSell(Long accountId, Long transactionId, BigDecimal amount, String symbol) {
        SellMessage message = new SellMessage();
        message.accountId = accountId;
        message.transactionId = transactionId;
        message.amount = amount;
        message.symbol = symbol;
        messageService.sendSell(message);
    }
}
