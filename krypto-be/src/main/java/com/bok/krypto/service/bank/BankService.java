package com.bok.krypto.service.bank;

import com.bok.bank.integration.grpc.AccountInfoResponse;
import com.bok.bank.integration.grpc.AuthorizationResponse;
import com.bok.bank.integration.grpc.Currency;
import com.bok.bank.integration.grpc.Money;
import com.bok.bank.integration.message.BankDepositMessage;
import com.bok.bank.integration.message.BankWithdrawalMessage;
import com.bok.krypto.grpc.client.BankGrpcClient;
import com.bok.krypto.service.interfaces.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
public class BankService {

    @Autowired
    BankGrpcClient bankGrpcClient;

    @Autowired
    MessageService messageService;

    public AuthorizationResponse authorize(Long accountId, UUID publicTransactionId, String cardToken, com.bok.bank.integration.util.Money money, String fromMarket) {
        return bankGrpcClient.authorize(accountId, publicTransactionId, cardToken, money, fromMarket);
    }

    public void sendBankWithdrawal(BankWithdrawalMessage bankWithdrawalMessage) {
        messageService.sendBankWithdrawal(bankWithdrawalMessage);
    }

    public void sendBankDeposit(BankDepositMessage bankDepositMessage) {
        messageService.sendBankDeposit(bankDepositMessage);
    }

    public com.bok.bank.integration.util.Money convertMoney(com.bok.bank.integration.util.Money from, java.util.Currency to) {
        Money.Builder moneyBuilder = Money.newBuilder();
        moneyBuilder.setCurrency(Currency.valueOf(from.currency.getCurrencyCode()));
        moneyBuilder.setAmount(from.amount.doubleValue());
        Money response = bankGrpcClient.convertMoney(moneyBuilder.build(), Currency.valueOf(to.getCurrencyCode()));

        com.bok.bank.integration.util.Money converted = new com.bok.bank.integration.util.Money();
        converted.setAmount(BigDecimal.valueOf(response.getAmount()));
        converted.setCurrency(java.util.Currency.getInstance(response.getCurrency().toString()));
        return converted;
    }

    public AccountInfoResponse getAccountInfo(Long accountId) {
        return bankGrpcClient.getAccountInfo(accountId);
    }
}
