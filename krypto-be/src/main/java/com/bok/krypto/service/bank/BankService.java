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

import java.util.UUID;

@Slf4j
@Service
public class BankService {

    @Autowired
    BankGrpcClient bankGrpcClient;

    @Autowired
    MessageService messageService;

    public AuthorizationResponse authorize(Long accountId, UUID publicTransactionId, com.bok.bank.integration.util.Money money, String fromMarket) {
        return bankGrpcClient.authorize(accountId, publicTransactionId, money, fromMarket);
    }

    public void sendBankWithdrawal(BankWithdrawalMessage bankWithdrawalMessage) {
        messageService.sendBankWithdrawal(bankWithdrawalMessage);
    }

    public void sendBankDeposit(BankDepositMessage bankDepositMessage) {
        messageService.sendBankDeposit(bankDepositMessage);
    }

    public Money convertMoney(Money from, Currency to) {
        return bankGrpcClient.convertMoney(from, to);
    }

    public AccountInfoResponse getAccountInfo(Long accountId) {
        return bankGrpcClient.getAccountInfo(accountId);
    }
}
