package com.bok.krypto.service.interfaces;

import com.bok.integration.krypto.PurchaseRequestDTO;
import com.bok.integration.krypto.dto.*;

public interface MarketService {

    TransactionDTO buy(Long userId, PurchaseRequestDTO purchaseRequestDTO);

    TransactionDTO sell(Long userId, SellRequestDTO sellRequestDTO);

    PriceResponseDTO getKryptoPrice(String symbol);

    PricesResponseDTO getKryptoPrices(PricesRequestDTO requestDTO);

    KryptoInfoDTO getKryptoInfo(String symbol);

    KryptoInfosDTO getKryptoInfos(KryptoInfosRequestDTO requestDTO);

    HistoricalDataDTO getKryptoHistoricalData(HistoricalDataRequestDTO requestDTO);

}
