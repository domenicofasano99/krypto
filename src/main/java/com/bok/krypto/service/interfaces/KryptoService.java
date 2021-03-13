package com.bok.krypto.service.interfaces;

import com.bok.krypto.dto.*;

public interface KryptoService {
    PricesResponseDTO getKryptoPrices(PricesRequestDTO requestDTO);

    PriceResponseDTO getKryptoPrice(PriceRequestDTO priceRequestDTO);

    KryptoInfoDTO getKryptoInfo(KryptoInfoRequestDTO requestDTO);

    KryptoInfosDTO getKryptoInfos(KryptoInfosRequestDTO requestDTO);
}
