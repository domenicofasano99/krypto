package com.bok.krypto.service.bank;

import com.bok.bank.integration.grpc.AccountInfoRequest;
import com.bok.bank.integration.grpc.AccountInfoResponse;
import com.bok.bank.integration.grpc.AuthorizationRequest;
import com.bok.bank.integration.grpc.AuthorizationResponse;
import com.bok.bank.integration.grpc.BankGrpc;
import com.bok.bank.integration.grpc.Currency;
import com.bok.bank.integration.grpc.Money;
import com.bok.bank.integration.message.BankDepositMessage;
import com.bok.bank.integration.message.BankWithdrawalMessage;
import com.bok.bank.integration.util.AuthorizationException;
import com.bok.krypto.service.interfaces.MessageService;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class BankService {

    @GrpcClient("bank")
    BankGrpc.BankBlockingStub bankBlockingStub;

    @Autowired
    MessageService messageService;

    public AuthorizationResponse authorize(Long accountId, UUID publicTransactionId, com.bok.bank.integration.util.Money money, String fromMarket) {

        AuthorizationRequest.Builder authorizationRequestBuilder = AuthorizationRequest.newBuilder();
        Money.Builder moneyBuilder = Money.newBuilder();

        authorizationRequestBuilder.setAccountId(accountId);
        authorizationRequestBuilder.setExtTransactionId(publicTransactionId.toString());
        moneyBuilder.setCurrency(Currency.USD).setAmount(money.getAmount().doubleValue()).build();
        authorizationRequestBuilder.setMoney(moneyBuilder);
        authorizationRequestBuilder.setFromMarket(fromMarket);
        try {
            return bankBlockingStub.authorize(authorizationRequestBuilder.build());
        } catch (Exception e) {
            log.error("Error while authorizing transaction");
            throw new AuthorizationException("Error while authorizing transaction, try again");
        }

    }

    public AccountInfoResponse getAccountDetails(Long accountId) {
        AccountInfoRequest.Builder requestBuilder = AccountInfoRequest.newBuilder();
        requestBuilder.setAccountId(accountId);
        return bankBlockingStub.getAccountInfo(requestBuilder.build());
    }

    public void sendBankWithdrawal(BankWithdrawalMessage bankWithdrawalMessage) {
        messageService.sendBankWithdrawal(bankWithdrawalMessage);
    }

    public void sendBankDeposit(BankDepositMessage bankDepositMessage) {
        messageService.sendBankDeposit(bankDepositMessage);
    }
}
