package com.bok.krypto.grpc.client;

import com.bok.bank.integration.grpc.*;
import com.bok.bank.integration.message.BankDepositMessage;
import com.bok.bank.integration.message.BankWithdrawalMessage;
import com.bok.bank.integration.util.AuthorizationException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class BankGrpcClient {

    @GrpcClient("bank")
    BankGrpc.BankBlockingStub bankBlockingStub;

    public AuthorizationResponse authorize(Long accountId, UUID extTransactionId) {
        AuthorizationRequest.Builder requestBuilder = AuthorizationRequest.newBuilder();
        requestBuilder.setAccountId(accountId);
        requestBuilder.setExtTransactionId(extTransactionId.toString());

        Money.Builder moneyBuilder = Money.newBuilder();
        return bankBlockingStub.authorize(AuthorizationRequest.newBuilder().build());
    }

    public AccountInfoResponse getAccountInfo(Long accountId) {
        AccountInfoRequest.Builder requestBuilder = AccountInfoRequest.newBuilder();
        requestBuilder.setAccountId(accountId);
        return bankBlockingStub.getAccountInfo(requestBuilder.build());
    }

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


    public Money convertMoney(Money from, Currency to) {
        ConversionRequest.Builder conversionRequest = ConversionRequest.newBuilder();
        conversionRequest.setFrom(from).setTo(to);
        return bankBlockingStub.convertMoney(conversionRequest.build());
    }

}
