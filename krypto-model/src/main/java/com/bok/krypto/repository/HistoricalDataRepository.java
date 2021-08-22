package com.bok.krypto.repository;

import com.bok.krypto.model.HistoricalData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.List;

public interface HistoricalDataRepository extends JpaRepository<HistoricalData, Long> {


    @Query("select h from HistoricalData h where h.timestamp between :timestampStart and :timestampEnd and upper(h.krypto.symbol) = upper(:symbol)")
    List<HistoricalData> findHistoricalDataBetween(@NonNull Instant timestampStart, @NonNull Instant timestampEnd, @NonNull String symbol);


    List<HistoricalData> findHistoricalDataByTimestampAfterAndTimestampBeforeAndKrypto_Symbol(@NonNull Instant timestampStart, @NonNull Instant timestampEnd, @NonNull String symbol);

    List<HistoricalData> findByKrypto_Symbol(String krypto_symbol);

}
