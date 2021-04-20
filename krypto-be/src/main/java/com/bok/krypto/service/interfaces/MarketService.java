package com.bok.krypto.service.interfaces;

import com.bok.integration.krypto.PurchaseRequestDTO;
import com.bok.integration.krypto.dto.HistoricalDataDTO;
import com.bok.integration.krypto.dto.HistoricalDataRequestDTO;
import com.bok.integration.krypto.dto.KryptoInfoDTO;
import com.bok.integration.krypto.dto.KryptoInfosDTO;
import com.bok.integration.krypto.dto.KryptoInfosRequestDTO;
import com.bok.integration.krypto.dto.PriceResponseDTO;
import com.bok.integration.krypto.dto.PricesRequestDTO;
import com.bok.integration.krypto.dto.PricesResponseDTO;
import com.bok.integration.krypto.dto.SellRequestDTO;
import com.bok.integration.krypto.dto.TransactionDTO;

public interface MarketService {

    TransactionDTO buy(Long userId, PurchaseRequestDTO purchaseRequestDTO);

    TransactionDTO sell(Long userId, SellRequestDTO sellRequestDTO);

    PriceResponseDTO getKryptoPrice(String symbol);

    PricesResponseDTO getKryptoPrices(PricesRequestDTO requestDTO);

    KryptoInfoDTO getKryptoInfo(String symbol);

    KryptoInfosDTO getKryptoInfos(KryptoInfosRequestDTO requestDTO);

    HistoricalDataDTO getKryptoHistoricalData(HistoricalDataRequestDTO requestDTO);

}
