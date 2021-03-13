package com.bok.krypto.helper;

import com.bok.krypto.core.Constants;
import com.bok.krypto.dto.PriceResponseDTO;
import com.bok.krypto.dto.PricesResponseDTO;
import com.bok.krypto.repository.KryptoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class KryptoHelper {

    @Autowired
    KryptoRepository kryptoRepository;

    public PricesResponseDTO getPrices(List<String> symbols) {
        List<PriceResponseDTO> prices = new ArrayList<>();
        for (String symbol : symbols) {
            prices.add(new PriceResponseDTO(symbol, getKryptoPrice(symbol)));
        }
        return new PricesResponseDTO(prices);
    }

    public PriceResponseDTO getPrice(String symbol) {
        return new PriceResponseDTO(symbol, getKryptoPrice(symbol));
    }

    @Cacheable(value = Constants.PRICES, key = "#symbol")
    public BigDecimal getKryptoPrice(String symbol) {
        KryptoRepository.Projection.KryptoPrice price = kryptoRepository.findPriceBySymbol(symbol);
        return price.getPrice();
    }

    public Boolean existsBySymbol(String symbol) {
        return kryptoRepository.existsBySymbol(symbol);
    }

}
