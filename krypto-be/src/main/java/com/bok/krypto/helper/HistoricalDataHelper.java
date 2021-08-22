package com.bok.krypto.helper;

import com.bok.krypto.exception.KryptoNotFoundException;
import com.bok.krypto.integration.internal.dto.HistoricalDataDTO;
import com.bok.krypto.integration.internal.dto.RecordDTO;
import com.bok.krypto.model.HistoricalData;
import com.bok.krypto.model.Krypto;
import com.bok.krypto.repository.HistoricalDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class HistoricalDataHelper {

    @Autowired
    HistoricalDataRepository historicalDataRepository;

    @Autowired
    KryptoHelper kryptoHelper;


    public HistoricalDataDTO getKryptoHistoricalData(String symbol, Instant from, Instant until) {
        if (!kryptoHelper.existsBySymbol(symbol)) {
            throw new KryptoNotFoundException("Krypto " + symbol + " does not exist");
        }
        if (until.isBefore(from) || from.isAfter(until)) {
            throw new RuntimeException("Wrong period format");
        }

        //due to Hibernate problems the query with Instants and between is not working, the support for this kind of complex queries should be added in following versions
        List<HistoricalData> data = historicalDataRepository.findByKrypto_Symbol(symbol).stream()
                .filter(h -> h.getTimestamp().isAfter(from) && h.getTimestamp().isBefore(until))
                .collect(Collectors.toList());

        log.info("found {} records for Krypto {} between dates {} and {}", data.size(), symbol, from, until);
        HistoricalDataDTO dto = new HistoricalDataDTO();
        dto.symbol = symbol;
        dto.history = new ArrayList<>();
        dto.start = from;
        dto.end = until;
        for (HistoricalData datum : data) {
            RecordDTO record = new RecordDTO();
            record.id = datum.getId();
            record.price = datum.getPrice();
            record.instant = datum.getTimestamp();
            dto.history.add(record);
        }
        return dto;
    }
}
