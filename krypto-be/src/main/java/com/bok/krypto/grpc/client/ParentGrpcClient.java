package com.bok.krypto.grpc.client;

import com.bok.parent.integration.grpc.EmailRequest;
import com.bok.parent.integration.grpc.ParentGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
public class ParentGrpcClient {

    @GrpcClient("parent:3030")
    ParentGrpc.ParentBlockingStub parentBlockingStub;

    @GrpcClient("parent:3030")
    ParentGrpc.ParentFutureStub parentFutureStub;

    public String getEmailByAccountId(Long accountId) {
        EmailRequest.Builder emailRequestBuilder = EmailRequest.newBuilder();
        emailRequestBuilder.setAccountId(accountId);
        return parentBlockingStub.getEmail(emailRequestBuilder.build()).getEmail();
    }


}
