package com.bok.krypto.service.interfaces;

import com.bok.integration.krypto.dto.*;

public interface KryptoService {
    PricesResponseDTO getKryptoPrices(PricesRequestDTO requestDTO);

    PriceResponseDTO getKryptoPrice(String symbol);

    KryptoInfoDTO getKryptoInfo(KryptoInfoRequestDTO requestDTO);

    KryptoInfosDTO getKryptoInfos(KryptoInfosRequestDTO requestDTO);
}
