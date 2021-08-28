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

    @Query("SELECT t  FROM Transaction t WHERE t.creationTimestamp>=:from AND t.creationTimestamp<=:until and t.wallet.id=:walletId")
    List<Transaction> findByWallet_IdAndCreationTimestampBetween(@Param("walletId") Long wallet_id, @Param("from") Instant from, @Param("until") Instant until);

    List<Transaction> findByWalletId(Long walletId);

    @Query("select count(t.id) from Transaction t where t.status like 'AUTHORIZED'")
    Integer countAuthorizedTransactions();

    void deleteByWalletId(Long wallet_id);

    public static class Projection {
        public interface Status {
            Transaction.Status getStatus();

        }
    }
}
