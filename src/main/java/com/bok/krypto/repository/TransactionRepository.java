package com.bok.krypto.repository;

import com.bok.krypto.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("select t.status from Transaction t where t.id = :transactionId")
    Transaction.Status findStatusById(@Param("transactionId") Long transactionId);

    @Query("select t from Transaction t where t.status='PENDING' and t.type='TRANSFER'")
    List<Transaction> findAllPendingTransfers();

    public static class Projection {
        public interface Status {
            Transaction.Status getStatus();

        }
    }
}
