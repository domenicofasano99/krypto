package com.bok.krypto.core;

import com.bok.krypto.external.CoinMarketAPI;
import com.bok.krypto.external.CoinMarketDTO;
import com.bok.krypto.external.Datum;
import com.bok.krypto.helper.KryptoHelper;
import com.bok.krypto.model.HistoricalData;
import com.bok.krypto.model.Krypto;
import com.bok.krypto.repository.HistoricalDataRepository;
import com.bok.krypto.repository.KryptoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@Transactional
public class MarketData {
    @Autowired
    CoinMarketAPI coinMarketAPI;

    @Autowired
    KryptoHelper kryptoHelper;

    @Autowired
    KryptoRepository kryptoRepository;

    @Autowired
    HistoricalDataRepository historicalDataRepository;


    //@Scheduled(fixedDelay = 300000, initialDelay = 1000)
    //@Scheduled(fixedDelay = 3000000, initialDelay = 1000)
    public void fetchData() {
        CoinMarketDTO data = coinMarketAPI.fetch();
        log.info("retrieved {} kryptocurrencies from coinMarket", data.data.size());
        Map<String, Krypto> kryptoMap = parseAndUpdateData(data);
        log.info("updating {} kryptocurrencies", kryptoMap.size());
        kryptoRepository.saveAll(kryptoMap.values());
    }


    public Map<String, Krypto> parseAndUpdateData(CoinMarketDTO coinMarketDTO) {
        Map<String, Krypto> kryptoMap = new HashMap<>();
        for (Datum d : coinMarketDTO.data) {
            Optional<Krypto> kOptional = kryptoRepository.findBySymbol(d.symbol);
            Krypto k;
            if (kOptional.isPresent()) {
                k = kOptional.get();
                k.setPrice(new BigDecimal(d.quote.USD.price));

            } else {
                k = new Krypto(d.name, d.symbol, new BigDecimal(d.quote.USD.price));
            }
            k.addHistoricalData(new HistoricalData(k, k.getPrice().doubleValue()));
            kryptoMap.put(k.getName(), k);
        }
        return kryptoMap;
    }
}
