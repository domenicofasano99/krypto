package com.bok.krypto.helper;

import com.bok.krypto.exception.KryptoNotFoundException;
import com.bok.krypto.integration.internal.dto.KryptoInfoDTO;
import com.bok.krypto.integration.internal.dto.KryptoInfosDTO;
import com.bok.krypto.integration.internal.dto.KryptoInfosRequestDTO;
import com.bok.krypto.integration.internal.dto.PriceResponseDTO;
import com.bok.krypto.integration.internal.dto.PricesRequestDTO;
import com.bok.krypto.integration.internal.dto.PricesResponseDTO;
import com.bok.krypto.integration.internal.dto.SymbolsDTO;
import com.bok.krypto.model.HistoricalData;
import com.bok.krypto.model.Krypto;
import com.bok.krypto.repository.HistoricalDataRepository;
import com.bok.krypto.repository.KryptoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class KryptoHelper {

    @Autowired
    KryptoRepository kryptoRepository;

    @Autowired
    HistoricalDataRepository historicalDataRepository;


    public PricesResponseDTO getPrices(PricesRequestDTO requestDTO) {
        List<PriceResponseDTO> prices = new ArrayList<>();
        for (String symbol : requestDTO.symbols) {
            prices.add(new PriceResponseDTO(symbol, getKryptoPrice(symbol)));
        }
        return new PricesResponseDTO(prices);
    }

    public PriceResponseDTO getPrice(String symbol) {
        BigDecimal price = getKryptoPrice(symbol);
        return new PriceResponseDTO(symbol, price);
    }

    @Cacheable("krypto-price")
    public BigDecimal getKryptoPrice(String symbol) {
        KryptoRepository.Projection.KryptoPrice price = kryptoRepository.findPriceBySymbol(symbol);
        return price.getPrice();
    }

    public Boolean existsBySymbol(String symbol) {
        return kryptoRepository.existsBySymbol(symbol);
    }

    @Cacheable("krypto")
    public Krypto findBySymbol(String symbol) {
        return kryptoRepository.findBySymbolIgnoreCase(symbol).orElseThrow(() -> new KryptoNotFoundException("This Krypto does not exist"));
    }

    @Cacheable("krypto-info")
    public KryptoInfoDTO getKryptoInfo(String symbol) {
        Krypto k = findBySymbol(symbol);
        return new KryptoInfoDTO(k.getName(), k.getSymbol(), k.getNetworkFee(), k.getPrice(), k.getUpdateTimestamp());
    }

    public KryptoInfosDTO getKryptoInfos(KryptoInfosRequestDTO infosRequest) {

        List<KryptoInfoDTO> infos = new ArrayList<>();
        for (String symbol : infosRequest.symbols) {
            infos.add(getKryptoInfo(symbol));
        }
        return new KryptoInfosDTO(infos);
    }

    public Krypto findBySymbolOrNull(String symbol) {
        return kryptoRepository.findBySymbolIgnoreCase(symbol).orElse(null);
    }

    public void saveAll(Collection<Krypto> values) {
        kryptoRepository.saveAll(values);
    }

    public KryptoInfosDTO getKryptoInfos() {
        List<KryptoInfoDTO> infos = new ArrayList<>();
        List<Krypto> kryptos = kryptoRepository.findAll();
        for (Krypto k : kryptos) {
            infos.add(new KryptoInfoDTO(k.getName(), k.getSymbol(), k.getNetworkFee(), k.getPrice(), k.getUpdateTimestamp()));
        }
        return new KryptoInfosDTO(infos);
    }

    public SymbolsDTO getSymbolLegend() {
        return null;
    }

    @Transactional
    public void addHistoricalData(Krypto krypto, HistoricalData historicalData) {
        historicalData.setKrypto(krypto);
        historicalDataRepository.save(historicalData);
    }

    public Krypto saveOrUpdate(Krypto krypto) {
        return kryptoRepository.save(krypto);
    }
}
