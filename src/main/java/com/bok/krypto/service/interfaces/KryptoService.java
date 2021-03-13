package com.bok.krypto.service.interfaces;

import com.bok.krypto.dto.PriceRequestDTO;
import com.bok.krypto.dto.PriceResponseDTO;
import com.bok.krypto.dto.PricesRequestDTO;
import com.bok.krypto.dto.PricesResponseDTO;

public interface KryptoService {
    PricesResponseDTO getKryptoPrices(PricesRequestDTO requestDTO);

    PriceResponseDTO getKryptoPrice(PriceRequestDTO priceRequestDTO);
}
