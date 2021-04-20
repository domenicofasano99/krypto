package com.bok.krypto.service;

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
import com.bok.krypto.helper.AccountHelper;
import com.bok.krypto.helper.HistoricalDataHelper;
import com.bok.krypto.helper.KryptoHelper;
import com.bok.krypto.helper.MarketHelper;
import com.bok.krypto.helper.WalletHelper;
import com.bok.krypto.service.interfaces.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MarketServiceImpl implements MarketService {

    @Autowired
    MarketHelper marketHelper;

    @Autowired
    WalletHelper walletHelper;

    @Autowired
    AccountHelper accountHelper;

    @Autowired
    KryptoHelper kryptoHelper;

    @Autowired
    HistoricalDataHelper historicalDataHelper;

    @Override
    public TransactionDTO buy(Long userId, PurchaseRequestDTO purchaseRequestDTO) {
        return marketHelper.buy(userId, purchaseRequestDTO);
    }

    @Override
    public TransactionDTO sell(Long userId, SellRequestDTO sellRequestDTO) {
        return marketHelper.sell(userId, sellRequestDTO);
    }

    @Override
    public PricesResponseDTO getKryptoPrices(PricesRequestDTO requestDTO) {
        return kryptoHelper.getPrices(requestDTO);
    }

    @Override
    public PriceResponseDTO getKryptoPrice(String symbol) {
        return kryptoHelper.getPrice(symbol);
    }

    @Override
    public KryptoInfoDTO getKryptoInfo(String symbol) {
        return kryptoHelper.getKryptoInfo(symbol);
    }

    @Override
    public KryptoInfosDTO getKryptoInfos(KryptoInfosRequestDTO requestDTO) {
        return kryptoHelper.getKryptoInfos(requestDTO);
    }

    @Override
    public HistoricalDataDTO getKryptoHistoricalData(HistoricalDataRequestDTO requestDTO) {
        return historicalDataHelper.getKryptoHistoricalData(requestDTO.symbol, requestDTO.start, requestDTO.end);
    }
}
