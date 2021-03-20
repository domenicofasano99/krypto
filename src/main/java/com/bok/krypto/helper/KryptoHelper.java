package com.bok.krypto.helper;

import com.bok.integration.krypto.dto.KryptoInfoDTO;
import com.bok.integration.krypto.dto.KryptoInfosDTO;
import com.bok.integration.krypto.dto.PriceResponseDTO;
import com.bok.integration.krypto.dto.PricesResponseDTO;
import com.bok.krypto.exception.KryptoNotFoundException;
import com.bok.krypto.model.Krypto;
import com.bok.krypto.repository.KryptoRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        BigDecimal price = getKryptoPrice(symbol);
        return new PriceResponseDTO(symbol, price);
    }

    public BigDecimal getKryptoPrice(String symbol) {
        KryptoRepository.Projection.KryptoPrice price = kryptoRepository.findPriceBySymbol(symbol);
        return price.getPrice();
    }

    public Boolean existsBySymbol(String symbol) {
        return kryptoRepository.existsBySymbol(symbol);
    }

    public Krypto findBySymbol(String symbol) {
        return kryptoRepository.findBySymbol(symbol).orElseThrow(() -> new KryptoNotFoundException("This Krypto does not exist"));
    }

    public KryptoInfoDTO getKryptoInfo(String symbol) {
        Krypto k = findBySymbol(symbol);
        return new KryptoInfoDTO(k.getName(), k.getSymbol(), k.getNetworkFee(), k.getPrice(), k.getUpdateTimestamp());
    }

    public KryptoInfosDTO getKryptoInfos(List<String> symbols) {
        List<KryptoInfoDTO> infos = new ArrayList<>();
        for (String symbol : symbols) {
            infos.add(getKryptoInfo(symbol));
        }
        return new KryptoInfosDTO(infos);
    }

}
