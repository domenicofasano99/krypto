package com.bok.krypto.repository;

import com.bok.krypto.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("select t.status from Transaction t where t.id = :transactionId")
    Transaction.Status findStatusById(@Param("transactionId") Long transactionId);


    @Query("select count(t.id) from Transaction t where t.status like 'PENDING'")
    Integer countPendingTransactions();

    Optional<Transaction> findByPublicId(UUID publicId);

    List<Transaction> findByWallet_IdAndCreationTimestampBetween(Long wallet_id, Instant creationTimestamp, Instant creationTimestamp2);

    public static class Projection {
        public interface Status {
            Transaction.Status getStatus();

        }
    }
}
