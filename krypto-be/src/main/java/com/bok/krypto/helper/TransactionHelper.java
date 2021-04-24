package com.bok.krypto.helper;

import com.bok.krypto.communication.messages.TransactionMessage;
import com.bok.krypto.model.Transaction;
import com.bok.krypto.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionHelper {

    @Autowired
    TransactionRepository transactionRepository;

    public void handleMessage(TransactionMessage transactionMessage) {
    }

    public Transaction saveOrUpdate(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Transaction findById(Long transactionId) {
        return transactionRepository.findById(transactionId).orElseThrow(() -> new RuntimeException("Transaction not found"));
    }
}
