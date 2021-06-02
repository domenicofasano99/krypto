package com.bok.krypto.grpc.client;

import com.bok.bank.integration.grpc.AuthorizationRequest;
import com.bok.bank.integration.grpc.BankGrpc;
import com.bok.bank.integration.grpc.Money;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BankGprcClient {

    @GrpcClient("bank")
    BankGrpc.BankBlockingStub bankBlockingStub;

    public void authorize(Long accountId, UUID extTransactionId) {
        AuthorizationRequest.Builder requestBuilder = AuthorizationRequest.newBuilder();
        requestBuilder.setAccountId(accountId);
        requestBuilder.setExtTransactionId(extTransactionId.toString());

        Money.Builder moneyBuilder = Money.newBuilder();
        bankBlockingStub.authorize(AuthorizationRequest.newBuilder().build());
    }
}
