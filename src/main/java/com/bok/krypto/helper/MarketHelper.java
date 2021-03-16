package com.bok.krypto.helper;

import com.bok.integration.EmailMessage;
import com.bok.integration.krypto.PurchaseRequestDTO;
import com.bok.integration.krypto.dto.SellRequestDTO;
import com.bok.integration.krypto.dto.TransactionDTO;
import com.bok.krypto.exception.InsufficientBalanceException;
import com.bok.krypto.messaging.messages.MarketMessage;
import com.bok.krypto.messaging.messages.PurchaseMessage;
import com.bok.krypto.messaging.messages.SellMessage;
import com.bok.krypto.model.Transaction;
import com.bok.krypto.model.User;
import com.bok.krypto.model.Wallet;
import com.bok.krypto.repository.TransactionRepository;
import com.bok.krypto.service.interfaces.MessageService;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.bok.krypto.exception.ErrorCodes.*;

@Component
@Slf4j
public class MarketHelper {

    @Autowired
    WalletHelper walletHelper;

    @Autowired
    KryptoHelper kryptoHelper;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserHelper userHelper;

    @Autowired
    MessageService messageService;


    public TransactionDTO buy(Long userId, PurchaseRequestDTO purchaseRequestDTO) {
        Preconditions.checkNotNull(purchaseRequestDTO);
        Preconditions.checkNotNull(purchaseRequestDTO.symbol);
        Preconditions.checkNotNull(purchaseRequestDTO.amount);
        Preconditions.checkNotNull(purchaseRequestDTO.amount);
        Preconditions.checkArgument(userHelper.existsById(userId), USER_DOES_NOT_EXIST);
        Preconditions.checkArgument(kryptoHelper.existsBySymbol(purchaseRequestDTO.symbol), KRYPTO_DOES_NOT_EXIST);
        Preconditions.checkArgument(purchaseRequestDTO.amount.compareTo(BigDecimal.ZERO) > 0, NEGATIVE_AMOUNT_GIVEN);

        Transaction t = new Transaction(Transaction.Type.BUY, Transaction.Status.PENDING);
        t = transactionRepository.save(t);
        MarketMessage message = new MarketMessage();
        message.userId = userId;
        message.transactionId = t.getId();
        message.amount = purchaseRequestDTO.amount;
        message.symbol = purchaseRequestDTO.symbol;
        messageService.send(message);
        return new TransactionDTO(t.getId(), t.status.name(), t.type.name(), purchaseRequestDTO.amount);
    }

    public void handle(PurchaseMessage purchaseMessage) {
        log.info("Processing purchase {}", purchaseMessage);
        Transaction t = transactionRepository.findById(purchaseMessage.transactionId).orElseThrow(() -> new RuntimeException("This transaction should have been persisted before"));
        Wallet destination = walletHelper.findByUserIdAndSymbol(purchaseMessage.userId, purchaseMessage.symbol);
        User user = userHelper.findById(purchaseMessage.userId);
        t.setSourceWallet(destination);
        t.setAmount(purchaseMessage.amount);
        t.setUser(user);

        try {
            walletHelper.deposit(destination, purchaseMessage.amount);
        } catch (InsufficientBalanceException ex) {
            EmailMessage email = new EmailMessage();
            email.subject = "Insufficient Balance in your account";
            email.to = user.getEmail();
            email.text = "Your PURCHASE transaction of " + purchaseMessage.amount + " " + purchaseMessage.symbol + " has been DECLINED due to insufficient balance.";
            t.setStatus(Transaction.Status.REJECTED);
            messageService.send(email);
        }
        EmailMessage email = new EmailMessage();
        email.subject = "Transfer executed";
        email.to = user.getEmail();
        email.text = "Your PURCHASE of " + purchaseMessage.amount + " " + purchaseMessage.symbol + " has been ACCEPTED.";
        t.setStatus(Transaction.Status.SETTLED);
        transactionRepository.saveAndFlush(t);
        messageService.send(email);
    }

    public TransactionDTO sell(Long userId, SellRequestDTO sellRequestDTO) {
        Preconditions.checkNotNull(sellRequestDTO);
        Preconditions.checkNotNull(sellRequestDTO.userId);
        Preconditions.checkNotNull(sellRequestDTO.symbol);
        Preconditions.checkNotNull(sellRequestDTO.amount);
        Preconditions.checkArgument(userHelper.existsById(userId), USER_DOES_NOT_EXIST);
        Preconditions.checkArgument(kryptoHelper.existsBySymbol(sellRequestDTO.symbol), KRYPTO_DOES_NOT_EXIST);
        Preconditions.checkArgument(sellRequestDTO.amount.compareTo(BigDecimal.ZERO) <= 0, "Cannot SELL a negative amount.");


        Transaction t = new Transaction(Transaction.Type.SELL, Transaction.Status.PENDING);
        t = transactionRepository.save(t);
        MarketMessage message = new MarketMessage();
        message.userId = userId;
        message.transactionId = t.getId();
        message.amount = sellRequestDTO.amount;
        message.symbol = sellRequestDTO.symbol;
        messageService.send(message);
        return new TransactionDTO(t.getId(), t.status.name(), t.type.name(), sellRequestDTO.amount);


    }

    public void handle(SellMessage sellMessage) {
        log.info("Processing sell {}", sellMessage);
        Transaction t = transactionRepository.findById(sellMessage.transactionId).orElseThrow(() -> new RuntimeException("This transaction should have been persisted before"));
        Wallet source = walletHelper.findByUserIdAndSymbol(sellMessage.userId, sellMessage.symbol);
        User user = userHelper.findById(sellMessage.userId);
        t.setSourceWallet(source);
        t.setAmount(sellMessage.amount);
        t.setUser(user);

        try {
            walletHelper.withdraw(source, sellMessage.amount);
        } catch (InsufficientBalanceException ex) {
            EmailMessage email = new EmailMessage();
            email.subject = "Insufficient Balance in your account";
            email.to = user.getEmail();
            email.text = "Your SELL transaction of " + sellMessage.amount + " " + sellMessage.symbol + " has been DECLINED due to insufficient balance.";
            t.setStatus(Transaction.Status.REJECTED);
            messageService.send(email);
        }
        EmailMessage email = new EmailMessage();
        email.subject = "Transfer executed";
        email.to = user.getEmail();
        email.text = "Your SELL of " + sellMessage.amount + " " + sellMessage.symbol + " has been ACCEPTED.";
        t.setStatus(Transaction.Status.SETTLED);
        transactionRepository.saveAndFlush(t);
        messageService.send(email);
    }
}
