package com.bok.krypto.service.interfaces;

import com.bok.krypto.integration.internal.dto.HistoricalDataDTO;
import com.bok.krypto.integration.internal.dto.KryptoInfoDTO;
import com.bok.krypto.integration.internal.dto.KryptoInfosDTO;
import com.bok.krypto.integration.internal.dto.KryptoInfosRequestDTO;
import com.bok.krypto.integration.internal.dto.PriceResponseDTO;
import com.bok.krypto.integration.internal.dto.PricesRequestDTO;
import com.bok.krypto.integration.internal.dto.PricesResponseDTO;
import com.bok.krypto.integration.internal.dto.PurchaseRequestDTO;
import com.bok.krypto.integration.internal.dto.SellRequestDTO;
import com.bok.krypto.integration.internal.dto.SymbolsDTO;
import com.bok.krypto.integration.internal.dto.ActivityDTO;

import java.time.Instant;

public interface MarketService {

    ActivityDTO buy(Long accountId, PurchaseRequestDTO purchaseRequestDTO);

    ActivityDTO sell(Long accountId, SellRequestDTO sellRequestDTO);

    PriceResponseDTO getKryptoPrice(String symbol);

    PricesResponseDTO getKryptoPrices(PricesRequestDTO requestDTO);

    KryptoInfoDTO getKryptoInfo(String symbol);

    KryptoInfosDTO getKryptoInfos(KryptoInfosRequestDTO requestDTO);

    HistoricalDataDTO getKryptoHistoricalData(String symbol, Instant startDate, Instant endDate);

    KryptoInfosDTO getAllKryptoInfos();

    SymbolsDTO getSymbolLegend();
}
