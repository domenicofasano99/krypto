package com.bok.krypto.helper;

import com.bok.krypto.exception.KryptoNotFoundException;
import com.bok.krypto.integration.internal.dto.HistoricalDataDTO;
import com.bok.krypto.integration.internal.dto.RecordDTO;
import com.bok.krypto.repository.HistoricalDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;

@Component
public class HistoricalDataHelper {

    @Autowired
    HistoricalDataRepository historicalDataRepository;

    @Autowired
    KryptoHelper kryptoHelper;


    public HistoricalDataDTO getKryptoHistoricalData(String symbol, Instant start, Instant end) {
        if (!kryptoHelper.existsBySymbol(symbol)) {
            throw new KryptoNotFoundException("Krypto " + symbol + " does not exist");
        }
        List<HistoricalDataRepository.Projection.HistoricalDataProjection> data = historicalDataRepository.findHistoricalDataByKrypto_SymbolAndRecordTimestampBetween(symbol, start, end);
        HistoricalDataDTO dto = new HistoricalDataDTO();
        dto.history = new ArrayList<>();
        dto.start = start;
        dto.end = end;
        for (HistoricalDataRepository.Projection.HistoricalDataProjection datum : data) {
            RecordDTO record = new RecordDTO();
            record.id = datum.getId();
            record.price = datum.getPrice().doubleValue();
            record.instant = datum.getRecordTimestamp();
            record.marketCap = datum.getMarketCap();
            dto.history.add(record);
        }
        return dto;
    }
}
