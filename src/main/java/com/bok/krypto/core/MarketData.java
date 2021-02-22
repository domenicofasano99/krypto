package com.bok.krypto.core;

import com.bok.krypto.external.CoinMarketAPI;
import com.bok.krypto.external.CoinMarketDTO;
import com.bok.krypto.external.Datum;
import com.bok.krypto.helper.KryptoHelper;
import com.bok.krypto.model.Krypto;
import com.bok.krypto.model.KryptoHistory;
import com.bok.krypto.repository.KryptoHistoryRepository;
import com.bok.krypto.repository.KryptoRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MarketData {
    @Autowired
    CoinMarketAPI coinMarketAPI;

    @Autowired
    KryptoHelper kryptoHelper;

    @Autowired
    KryptoRepository kryptoRepository;

    @Autowired
    KryptoHistoryRepository kryptoHistoryRepository;

    @Scheduled(fixedDelay = 300000, initialDelay = 1000)
    public void fetchData() {
        CoinMarketDTO data = coinMarketAPI.fetch();
        List<Pair<String, BigDecimal>> newPrices = parseData(data);
        updatePrices(newPrices);
    }


    public List<Pair<String, BigDecimal>> parseData(CoinMarketDTO coinMarketDTO) {
        Set<KryptoHistory> newData = new HashSet<>();
        List<Pair<String, BigDecimal>> kryptoPrices = new ArrayList<>();
        coinMarketDTO.data.forEach(datum -> {
            KryptoHistory extracted = extractFromDTO(datum);
            newData.add(extracted);
            kryptoPrices.add(new ImmutablePair<>(extracted.symbol, new BigDecimal(extracted.price)));
        });
        kryptoHistoryRepository.saveAll(newData);
        return kryptoPrices;
    }

    private KryptoHistory extractFromDTO(Datum datum) {
        KryptoHistory krypto = new KryptoHistory();
        krypto.name = datum.name;
        krypto.price = datum.quote.uSD.price;
        krypto.marketCap = datum.quote.uSD.marketCap;
        krypto.symbol = datum.symbol;
        return krypto;
    }

    private void updatePrices(List<Pair<String, BigDecimal>> prices) {
        List<String> codes = prices.stream().map(Pair::getKey).collect(Collectors.toList());
        List<Krypto> toUpdate = kryptoRepository.findBySymbolIn(codes);
        for (Pair<String, BigDecimal> p :
                prices) {
            toUpdate.stream().filter(krypto -> krypto.getSymbol().equals(p.getKey())).findFirst().get()
                    .setPrice(p.getValue());
        }
        kryptoRepository.saveAll(toUpdate);
    }
}
