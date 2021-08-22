package com.bok.krypto.service.parent;

import com.bok.krypto.grpc.client.ParentGrpcClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ParentService {

    @Autowired
    ParentGrpcClient parentGrpcClient;

    public String getEmailByAccountId(Long accountId) {
        return parentGrpcClient.getEmailByAccountId(accountId);
    }

}
