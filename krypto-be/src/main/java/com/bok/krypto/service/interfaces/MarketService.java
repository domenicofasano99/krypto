package com.bok.krypto.service.interfaces;

import com.bok.krypto.integration.internal.dto.HistoricalDataDTO;
import com.bok.krypto.integration.internal.dto.HistoricalDataRequestDTO;
import com.bok.krypto.integration.internal.dto.KryptoInfoDTO;
import com.bok.krypto.integration.internal.dto.KryptoInfosDTO;
import com.bok.krypto.integration.internal.dto.KryptoInfosRequestDTO;
import com.bok.krypto.integration.internal.dto.PriceResponseDTO;
import com.bok.krypto.integration.internal.dto.PricesRequestDTO;
import com.bok.krypto.integration.internal.dto.PricesResponseDTO;
import com.bok.krypto.integration.internal.dto.PurchaseRequestDTO;
import com.bok.krypto.integration.internal.dto.SellRequestDTO;
import com.bok.krypto.integration.internal.dto.TransactionDTO;
import org.apache.tomcat.jni.Local;

import java.time.LocalDate;

public interface MarketService {

    TransactionDTO buy(Long userId, PurchaseRequestDTO purchaseRequestDTO);

    TransactionDTO sell(Long userId, SellRequestDTO sellRequestDTO);

    PriceResponseDTO getKryptoPrice(String symbol);

    PricesResponseDTO getKryptoPrices(PricesRequestDTO requestDTO);

    KryptoInfoDTO getKryptoInfo(String symbol);

    KryptoInfosDTO getKryptoInfos(KryptoInfosRequestDTO requestDTO);

    HistoricalDataDTO getKryptoHistoricalData(String symbol, LocalDate startDate, LocalDate endDate);

    KryptoInfosDTO getAllKryptoInfos();
}
