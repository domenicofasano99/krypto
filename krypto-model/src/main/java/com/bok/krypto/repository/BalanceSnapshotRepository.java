package com.bok.krypto.repository;

import com.bok.krypto.model.BalanceSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BalanceSnapshotRepository extends JpaRepository<BalanceSnapshot, Long> {
    List<BalanceSnapshot> findByWallet_Id(Long wallet_id);
}
