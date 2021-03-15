package com.bok.krypto.helper;

import com.bok.krypto.messaging.TransactionMessage;
import com.bok.krypto.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionHelper {

    @Autowired
    TransactionRepository transactionRepository;

    public void handleMessage(TransactionMessage transactionMessage){

    }
}
