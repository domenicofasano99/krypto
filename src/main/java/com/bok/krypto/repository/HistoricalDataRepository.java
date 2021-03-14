package com.bok.krypto.repository;

import com.bok.krypto.model.HistoricalData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public interface HistoricalDataRepository extends JpaRepository<HistoricalData, Long> {


    List<Projection.HistoricalDataProjection> findHistoricalDataByKrypto_SymbolAndRecordTimestampBetween(String symbol, Instant start, Instant end);


    public class Projection {
        public interface HistoricalDataProjection {
            BigDecimal getPrice();

            Double getMarketCap();

            Instant getRecordTimestamp();
        }
    }

}
