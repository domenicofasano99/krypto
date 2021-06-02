package com.bok.krypto.grpc.server;

import com.bok.krypto.helper.AccountHelper;
import com.bok.krypto.integration.grpc.AccountCreationRequest;
import com.bok.krypto.integration.grpc.AccountCreationResponse;
import com.bok.krypto.integration.grpc.KryptoGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class KryptoGrpcServerService extends KryptoGrpc.KryptoImplBase {

    @Autowired
    AccountHelper accountHelper;

    @Override
    public void createAccount(AccountCreationRequest request, StreamObserver<AccountCreationResponse> responseObserver) {
        AccountCreationResponse.Builder responseBuilder = AccountCreationResponse.newBuilder();
        accountHelper.createAccount(request.getAccountId(), request.getEmail());
        responseObserver.onNext(responseBuilder.setCreated(true).build());
        responseObserver.onCompleted();
    }



}