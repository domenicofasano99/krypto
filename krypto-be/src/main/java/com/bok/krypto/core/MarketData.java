package com.bok.krypto.core;

import com.bok.krypto.integration.external.CoinMarketDTO;
import com.bok.krypto.integration.external.Datum;
import com.bok.krypto.model.HistoricalData;
import com.bok.krypto.model.Krypto;
import com.bok.krypto.repository.HistoricalDataRepository;
import com.bok.krypto.repository.KryptoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@Transactional
public class MarketData {
    @Autowired
    CoinMarketAPI coinMarketAPI;

    @Autowired
    KryptoRepository kryptoRepository;

    @Autowired
    HistoricalDataRepository historicalDataRepository;


    //@Scheduled(fixedDelay = 300000, initialDelay = 1000)
    @Scheduled(fixedDelay = 3000000, initialDelay = 1000)
    public void fetchData() {
        CoinMarketDTO data = coinMarketAPI.fetch();
        log.info("retrieved {} krypto-currencies from coinMarket", data.data.size());
        saveOrUpdate(data);
    }

    @CacheEvict(value = Constants.PRICES)
    public void saveOrUpdate(CoinMarketDTO coinMarketDTO) {
        List<Krypto> updatedKryptos = new ArrayList<>();
        for (Datum datum : coinMarketDTO.data) {
            Krypto krypto = kryptoRepository.findBySymbolIgnoreCase(datum.symbol).orElse(null);
            if (Objects.isNull(krypto)) {
                krypto = new Krypto(datum.name, datum.symbol);
            }
            krypto.setPrice(new BigDecimal(datum.quote.USD.price));

            HistoricalData dataRecord = new HistoricalData();
            dataRecord.setPrice(krypto.getPrice().doubleValue());
            dataRecord.setCirculatingSupply(datum.circulatingSupply);
            dataRecord.setKrypto(krypto);
            krypto.addHistoricalData(dataRecord);
            updatedKryptos.add(krypto);
        }
        kryptoRepository.saveAll(updatedKryptos);
    }
}
