package com.bok.krypto.helper;

import com.bok.krypto.model.Transaction;
import com.bok.krypto.model.Wallet;
import com.bok.krypto.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Component
public class TransactionHelper {

    @Autowired
    TransactionRepository transactionRepository;

    public Transaction saveOrUpdate(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Transaction findByPublicId(UUID transactionId) {
        return transactionRepository.findByPublicId(transactionId).orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    public Transaction findById(Long transactionId) {
        return transactionRepository.findById(transactionId).orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    public List<Transaction> findByWalletIdAndDateBetween(Wallet wallet, LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findByWallet_IdAndCreationTimestampBetween(wallet.getId(), startDate.atStartOfDay().toInstant(ZoneOffset.UTC), endDate.atStartOfDay().toInstant(ZoneOffset.UTC));
    }
}
