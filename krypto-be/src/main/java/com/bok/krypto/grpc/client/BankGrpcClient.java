package com.bok.krypto.grpc.client;

import com.bok.bank.integration.grpc.AccountInfoRequest;
import com.bok.bank.integration.grpc.AccountInfoResponse;
import com.bok.bank.integration.grpc.AuthorizationRequest;
import com.bok.bank.integration.grpc.AuthorizationResponse;
import com.bok.bank.integration.grpc.BankGrpc;
import com.bok.bank.integration.grpc.ConversionRequest;
import com.bok.bank.integration.grpc.Currency;
import com.bok.bank.integration.grpc.Money;
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

    public AccountInfoResponse getAccountInfo(Long accountId) {
        AccountInfoRequest.Builder requestBuilder = AccountInfoRequest.newBuilder();
        requestBuilder.setAccountId(accountId);
        return bankBlockingStub.getAccountInfo(requestBuilder.build());
    }


    public AuthorizationResponse authorize(Long accountId, UUID publicTransactionId, String cardToken, com.bok.bank.integration.util.Money money, String fromMarket) {

        AuthorizationRequest.Builder authorizationRequestBuilder = AuthorizationRequest.newBuilder();
        Money.Builder moneyBuilder = Money.newBuilder();

        authorizationRequestBuilder.setAccountId(accountId);
        authorizationRequestBuilder.setExtTransactionId(publicTransactionId.toString());
        moneyBuilder.setCurrency(Currency.valueOf(money.getCurrency().getCurrencyCode()));
        moneyBuilder.setAmount(money.getAmount().doubleValue());
        moneyBuilder.build();
        authorizationRequestBuilder.setMoney(moneyBuilder);
        authorizationRequestBuilder.setFromMarket(fromMarket);
        authorizationRequestBuilder.setCardToken(cardToken);

        try {
            AuthorizationRequest authReq = authorizationRequestBuilder.build();
            log.info("authorizing {}", authReq);
            return bankBlockingStub.authorize(authReq);
        } catch (Exception e) {
            log.error("Error while authorizing transaction, exception: {}", e);
            throw new AuthorizationException("Error while authorizing transaction, try again");
        }

    }


    public Money convertMoney(Money from, Currency to) {
        ConversionRequest.Builder conversionRequest = ConversionRequest.newBuilder();
        conversionRequest.setFrom(from).setTo(to);
        return bankBlockingStub.convertMoney(conversionRequest.build());
    }

}
