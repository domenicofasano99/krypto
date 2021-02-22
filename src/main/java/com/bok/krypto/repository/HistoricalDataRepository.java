package com.bok.krypto.repository;

import com.bok.krypto.model.HistoricalData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricalDataRepository extends JpaRepository<HistoricalData, Long> {
}
