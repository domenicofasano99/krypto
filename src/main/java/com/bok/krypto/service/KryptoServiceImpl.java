package com.bok.krypto.service;

import com.bok.integration.krypto.dto.*;
import com.bok.krypto.helper.HistoricalDataHelper;
import com.bok.krypto.helper.KryptoHelper;
import com.bok.krypto.service.interfaces.KryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KryptoServiceImpl implements KryptoService {

    @Autowired
    KryptoHelper kryptoHelper;

    @Autowired
    HistoricalDataHelper historicalDataHelper;

    @Override
    public PricesResponseDTO getKryptoPrices(PricesRequestDTO requestDTO) {
        return kryptoHelper.getPrices(requestDTO.symbols);
    }

    @Override
    public PriceResponseDTO getKryptoPrice(String symbol) {
        return kryptoHelper.getPrice(symbol);
    }

    @Override
    public KryptoInfoDTO getKryptoInfo(KryptoInfoRequestDTO requestDTO) {
        return kryptoHelper.getKryptoInfo(requestDTO.symbol);
    }

    @Override
    public KryptoInfosDTO getKryptoInfos(KryptoInfosRequestDTO requestDTO) {
        return kryptoHelper.getKryptoInfos(requestDTO.symbols);
    }

    @Override
    public HistoricalDataDTO getKryptoHistoricalData(HistoricalDataRequestDTO dataRequestDTO) {
        return historicalDataHelper.getKryptoHistoricalData(dataRequestDTO.symbol, dataRequestDTO.start, dataRequestDTO.end);
    }
}
