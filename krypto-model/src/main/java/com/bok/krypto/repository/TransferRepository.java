package com.bok.krypto.repository;

import com.bok.krypto.model.Activity;
import com.bok.krypto.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    @Query("SELECT status from Transfer where id =: transferId")
    Activity.Status findStatusById(@Param("transferId") Long transferId);

    @Query("SELECT COUNT(t.id) from Transfer t where t.status like 'PENDING'")
    Integer countPendingTransfers();

    Optional<Transfer> findByPublicId(UUID transferId);

    //TODO write the query to get all transfers with source or destination that ID
    @Query("SELECT t FROM Transfer t where (t.sourceWallet =: id or t.destinationWallet =:id) and t.creationTimestamp between :start and :end")
    List<Transfer> findByAndCreationTimestampBetween(Long id, Instant start, Instant end);
}
