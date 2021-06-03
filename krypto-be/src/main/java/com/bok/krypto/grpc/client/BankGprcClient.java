package com.bok.krypto.grpc.client;

import com.bok.bank.integration.grpc.AccountInfoRequest;
import com.bok.bank.integration.grpc.AccountInfoResponse;
import com.bok.bank.integration.grpc.AuthorizationRequest;
import com.bok.bank.integration.grpc.AuthorizationResponse;
import com.bok.bank.integration.grpc.BankGrpc;
import com.bok.bank.integration.grpc.Money;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BankGprcClient {

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

}
