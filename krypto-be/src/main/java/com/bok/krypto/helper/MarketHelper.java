package com.bok.krypto.helper;

import com.bok.bank.integration.grpc.AuthorizationResponse;
import com.bok.bank.integration.message.BankDepositMessage;
import com.bok.bank.integration.message.BankWithdrawalMessage;
import com.bok.bank.integration.util.Money;
import com.bok.krypto.exception.ErrorCodes;
import com.bok.krypto.exception.TransactionException;
import com.bok.krypto.exception.WalletNotFoundException;
import com.bok.krypto.integration.internal.dto.ActivityDTO;
import com.bok.krypto.integration.internal.dto.PurchaseRequestDTO;
import com.bok.krypto.integration.internal.dto.SellRequestDTO;
import com.bok.krypto.messaging.messages.PurchaseMessage;
import com.bok.krypto.messaging.messages.SellMessage;
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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.UUID;

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

    public ActivityDTO buy(Long accountId, PurchaseRequestDTO purchaseRequest) {
        Preconditions.checkArgument(accountHelper.existsById(accountId), ErrorCodes.USER_DOES_NOT_EXIST);
        Preconditions.checkArgument(kryptoHelper.existsBySymbol(purchaseRequest.symbol), ErrorCodes.KRYPTO_DOES_NOT_EXIST);
        Preconditions.checkArgument(purchaseRequest.amount.compareTo(BigDecimal.ZERO) > 0, ErrorCodes.NEGATIVE_AMOUNT_GIVEN);

        Money money = convertIntoMoney(purchaseRequest.amount, purchaseRequest.currencyCode);

        Transaction transaction = new Transaction(Transaction.Type.PURCHASE);
        transaction = transactionHelper.saveOrUpdate(transaction);

        AuthorizationResponse authorizationResponse = bankService.authorize(accountId, transaction.getPublicId(), purchaseRequest.cardToken, money, purchaseRequest.symbol);
        transaction.status = authorizationResponse.getAuthorized() ? Activity.Status.AUTHORIZED : Activity.Status.DECLINED;
        log.info("Received {} from Bank for transaction {}", transaction.status.name(), transaction.getId());
        transaction.setPublicId(UUID.fromString(authorizationResponse.getAuthorizationId()));
        transaction = transactionHelper.saveOrUpdate(transaction);

        if (authorizationResponse.getAuthorized()) {
            sendPurchase(accountId, transaction.getId(), purchaseRequest.amount, purchaseRequest.currencyCode, purchaseRequest.symbol);
        }
        return new ActivityDTO(transaction.getPublicId(), accountId, ActivityDTO.Type.PURCHASE, purchaseRequest.amount, transaction.status.name());
    }

    public void handle(PurchaseMessage message) {
        log.info("Processing purchase {}", message);

        Account account = accountHelper.findById(message.accountId);
        Krypto k = kryptoHelper.findBySymbol(message.kryptoSymbol);
        Transaction transaction = transactionHelper.findById(message.transactionId);
        Wallet destination;

        try {
            destination = walletHelper.findByAccountIdAndSymbol(message.accountId, message.kryptoSymbol);
        } catch (WalletNotFoundException we) {
            log.warn("account {} made a purchase of {} but does not have a wallet for krypto {}, creating a wallet for it", message.accountId, message.kryptoSymbol, message.kryptoSymbol);
            destination = walletHelper.createWallet(account, k);
        }

        Money money = convertIntoMoney(message.moneyAmount, message.currencyCode);
        BigDecimal usd = BigDecimal.valueOf(bankService.convertMoney(money.toGrpcMoney(), com.bok.bank.integration.grpc.Currency.USD).getAmount());
        BigDecimal kryptoAmount = getKryptoAmount(k, usd);

        transaction.setWallet(destination);
        transaction.setAmount(kryptoAmount);
        transaction.setAccount(account);
        transactionHelper.saveOrUpdate(transaction);

        EmailMessage email = new EmailMessage();
        try {
            walletHelper.deposit(destination, kryptoAmount);

            BankWithdrawalMessage bankWithdrawalMessage = new BankWithdrawalMessage(money, message.accountId, destination.getKrypto().getSymbol(), transaction.getPublicId());
            bankService.sendBankWithdrawal(bankWithdrawalMessage);
            log.info("Completed purchase ID:{} for {} of {} {}", message.transactionId, message.accountId, message.kryptoSymbol, message.moneyAmount);
            email.subject = "Purchase completed!";
            email.to = accountHelper.getEmailByAccountId(account.getId());
            email.body = "Your PURCHASE of " + money.amount + " " + money.getCurrency() + " of " + message.kryptoSymbol + " has been ACCEPTED.";
            transaction.setStatus(Activity.Status.SETTLED);

        } catch (TransactionException ex) {
            log.info("Purchase {} error, insufficient balance", message);
            email.subject = "Insufficient Balance in your account";
            email.to = accountHelper.getEmailByAccountId(account.getId());
            email.body = "Your PURCHASE transaction of " + money.amount + " " + money.getCurrency() + " of " + message.kryptoSymbol + " has been DECLINED due to insufficient balance.";
            transaction.setStatus(Activity.Status.DECLINED);
        }

        transactionHelper.saveOrUpdate(transaction);
        messageService.sendEmail(email);
    }


    @Transactional
    public ActivityDTO sell(Long accountId, SellRequestDTO request) {
        Preconditions.checkArgument(accountHelper.existsById(accountId), ErrorCodes.USER_DOES_NOT_EXIST);
        Preconditions.checkArgument(kryptoHelper.existsBySymbol(request.symbol), ErrorCodes.KRYPTO_DOES_NOT_EXIST);
        Preconditions.checkArgument(request.amount.compareTo(BigDecimal.ZERO) >= 0, "Cannot SELL a negative amount.");

        Transaction transaction = new Transaction(Transaction.Type.SELL);
        transaction = transactionHelper.saveOrUpdate(transaction);

        sendSell(accountId, transaction.getId(), request.amount, request.currencyCode, request.symbol);
        return new ActivityDTO(transaction.getPublicId(), accountId, ActivityDTO.Type.SELL, request.amount, transaction.status.name());
    }

    @Transactional
    public void handle(SellMessage message) {
        log.info("Processing sell {}", message);
        Transaction transaction = transactionHelper.findById(message.transactionId);
        Wallet source = walletHelper.findByAccountIdAndSymbol(message.accountId, message.kryptoSymbol);
        Account account = accountHelper.findById(message.accountId);
        transaction.setWallet(source);
        transaction.setAmount(message.moneyAmount);
        transaction.setAccount(account);

        String subject, to, text;
        try {
            walletHelper.withdraw(source, message.moneyAmount);
            Money amountToDeposit = convertIntoMoney(message.moneyAmount, message.currencyCode);
            BankDepositMessage bankDepositMessage = new BankDepositMessage(amountToDeposit, message.accountId, source.getKrypto().getSymbol());
            bankService.sendBankDeposit(bankDepositMessage);
            subject = "Sell executed";
            to = accountHelper.getEmailByAccountId(account.getId());
            text = "Your SELL of " + message.moneyAmount + " " + message.kryptoSymbol + " has been ACCEPTED.";
            transaction.setStatus(Activity.Status.SETTLED);
            log.info("SETTLED {}", transaction);

        } catch (TransactionException ex) {
            subject = "Insufficient KryptoBalance in your account";
            to = accountHelper.getEmailByAccountId(account.getId());
            text = "Your SELL transaction of " + message.moneyAmount + " " + message.kryptoSymbol + " has been DECLINED due to insufficient balance.";
            transaction.setStatus(Activity.Status.DECLINED);
            log.info("DECLINED {}", transaction);
        }
        log.info("transaction {} processed", transaction.getId());
        transactionHelper.saveOrUpdate(transaction);
        sendMarketEmail(subject, to, text);
    }

    @Transactional
    public Transaction emptyWallet(Account account, Wallet walletToEmpty) {

        BigDecimal amountToSell = walletToEmpty.getAvailableAmount();
        Transaction sellTransaction = new Transaction();
        sellTransaction.setAmount(amountToSell);
        sellTransaction.setAccount(account);
        Money money = convertKryptoIntoMoney(walletToEmpty.getKrypto(), amountToSell);
        //send message to bank to credit money USD
        bankService.sendBankDeposit(new BankDepositMessage(money, account.getId(), walletToEmpty.getKrypto().getSymbol()));
        transactionHelper.saveOrUpdate(sellTransaction);

        String subject, to, text;
        subject = "Wallet emptied";
        to = accountHelper.getEmailByAccountId(account.getId());
        text = "Your " + walletToEmpty.getKrypto().getSymbol() + " wallet has been emptied, you should receive " +
                "the converted amount in your bank account in a few minutes.";
        sendMarketEmail(subject, to, text);
        return sellTransaction;

    }

    public Money convertIntoMoney(BigDecimal amount, String currency) {
        return new Money(Currency.getInstance(currency), amount);
    }

    public Money convertKryptoIntoMoney(Krypto k, BigDecimal amount) {
        return new Money(Currency.getInstance("USD"), k.getPrice().multiply(amount));
    }


    public void sendMarketEmail(String subject, String email, String text) {
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.subject = subject;
        emailMessage.to = email;
        emailMessage.body = text;
        messageService.sendEmail(emailMessage);
    }

    private void sendPurchase(Long accountId, Long transactionId, BigDecimal moneyAmount, String currencyCode, String symbol) {
        PurchaseMessage message = new PurchaseMessage();
        message.accountId = accountId;
        message.transactionId = transactionId;
        message.moneyAmount = moneyAmount;
        message.kryptoSymbol = symbol;
        message.currencyCode = currencyCode;
        messageService.sendPurchase(message);
    }

    private void sendSell(Long accountId, Long transactionId, BigDecimal amount, String currency, String symbol) {
        SellMessage message = new SellMessage();
        message.accountId = accountId;
        message.transactionId = transactionId;
        message.moneyAmount = amount;
        message.currencyCode = currency;
        message.kryptoSymbol = symbol;
        messageService.sendSell(message);
    }

    private BigDecimal getKryptoAmount(Krypto k, BigDecimal usdMoneyAmount) {
        return usdMoneyAmount.divide(k.getPrice(), 10, RoundingMode.HALF_EVEN);
    }
}
