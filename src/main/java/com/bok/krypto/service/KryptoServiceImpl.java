package com.bok.krypto.service;

import com.bok.krypto.dto.PriceRequestDTO;
import com.bok.krypto.dto.PriceResponseDTO;
import com.bok.krypto.dto.PricesRequestDTO;
import com.bok.krypto.dto.PricesResponseDTO;
import com.bok.krypto.helper.KryptoHelper;
import com.bok.krypto.service.interfaces.KryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KryptoServiceImpl implements KryptoService {

    @Autowired
    KryptoHelper kryptoHelper;

    @Override
    public PricesResponseDTO getKryptoPrices(PricesRequestDTO requestDTO) {
        return kryptoHelper.getPrices(requestDTO.symbols);
    }

    @Override
    public PriceResponseDTO getKryptoPrice(PriceRequestDTO priceRequestDTO) {
        return kryptoHelper.getPrice(priceRequestDTO.symbol);
    }
}
