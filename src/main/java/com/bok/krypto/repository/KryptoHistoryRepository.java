package com.bok.krypto.repository;

import com.bok.krypto.model.KryptoHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KryptoHistoryRepository extends JpaRepository<KryptoHistory, Long> {
}
