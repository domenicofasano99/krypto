package com.bok.krypto.helper;

import com.bok.krypto.model.Transaction;
import com.bok.krypto.model.Wallet;
import com.bok.krypto.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TransactionHelper {

    @Autowired
    TransactionRepository transactionRepository;

    public Transaction saveOrUpdate(Transaction transaction) {
        log.info("saving {}", transaction);
        return transactionRepository.saveAndFlush(transaction);
    }

    public Transaction findByPublicId(UUID transactionId) {
        return transactionRepository.findByPublicId(transactionId).orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    public Transaction findById(Long transactionId) {
        return transactionRepository.findById(transactionId).orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    public List<Transaction> findByWalletIdAndDateBetween(Wallet wallet, Instant startDate, Instant endDate) {

        List<Transaction> transactions = transactionRepository.findByWalletId(wallet.getId());
        return transactions.stream()
                .filter(t -> t.getCreationTimestamp().isAfter(startDate)
                        && t.getCreationTimestamp().isBefore(endDate))
                .collect(Collectors.toList());

        //return transactionRepository.findByWallet_IdAndCreationTimestampBetween(wallet.getId(), startDate.atStartOfDay().toInstant(ZoneOffset.UTC), endDate.atStartOfDay().toInstant(ZoneOffset.UTC));
    }
}
