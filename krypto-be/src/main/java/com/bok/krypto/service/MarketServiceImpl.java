package com.bok.krypto.service;

import com.bok.krypto.helper.AccountHelper;
import com.bok.krypto.helper.HistoricalDataHelper;
import com.bok.krypto.helper.KryptoHelper;
import com.bok.krypto.helper.MarketHelper;
import com.bok.krypto.helper.WalletHelper;
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
import com.bok.krypto.service.interfaces.MarketService;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;

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
    public ActivityDTO buy(Long accountId, PurchaseRequestDTO purchaseRequestDTO) {
        Preconditions.checkNotNull(accountId);
        Preconditions.checkNotNull(purchaseRequestDTO, "Request body was empty");
        Preconditions.checkNotNull(purchaseRequestDTO.symbol, "Symbol cannot be null");
        Preconditions.checkNotNull(purchaseRequestDTO.amount, "Amount cannot be null");
        Preconditions.checkNotNull(purchaseRequestDTO.currencyCode, "Currency cannot be null");
        return marketHelper.buy(accountId, purchaseRequestDTO);
    }

    @Override
    public ActivityDTO sell(Long accountId, SellRequestDTO sellRequestDTO) {
        Preconditions.checkNotNull(accountId);
        Preconditions.checkNotNull(sellRequestDTO, "Request body was empty");
        Preconditions.checkNotNull(sellRequestDTO.symbol, "Symbol cannot be null");
        Preconditions.checkNotNull(sellRequestDTO.amount, "Amount cannot be null");
        Preconditions.checkNotNull(sellRequestDTO.currencyCode, "Currency cannot be null");
        return marketHelper.sell(accountId, sellRequestDTO);
    }

    @Override
    public PricesResponseDTO getKryptoPrices(PricesRequestDTO requestDTO) {
        Preconditions.checkNotNull(requestDTO.symbols, "Symbols list cannot be null");
        Preconditions.checkArgument(!requestDTO.symbols.isEmpty(), "Symbols list cannot be empty");
        return kryptoHelper.getPrices(requestDTO);
    }

    @Override
    public PriceResponseDTO getKryptoPrice(String symbol) {
        Preconditions.checkNotNull(symbol, "Symbol cannot be null");
        return kryptoHelper.getPrice(symbol);
    }

    @Override
    public KryptoInfoDTO getKryptoInfo(String symbol) {
        Preconditions.checkNotNull(symbol, "Symbol cannot be null");
        return kryptoHelper.getKryptoInfo(symbol);
    }

    @Override
    public KryptoInfosDTO getKryptoInfos(KryptoInfosRequestDTO requestDTO) {
        Preconditions.checkNotNull(requestDTO.symbols, "Symbols list cannot be null");
        Preconditions.checkArgument(!requestDTO.symbols.isEmpty(), "Symbols list cannot be empty");
        return kryptoHelper.getKryptoInfos(requestDTO);
    }

    //TODO fix query and remove transactional
    @Override
    @Transactional
    public HistoricalDataDTO getKryptoHistoricalData(String symbol, Instant startDate, Instant endDate) {
        Preconditions.checkNotNull(startDate, "Start date Instant cannot be null");
        Preconditions.checkNotNull(endDate, "End date Instant cannot be null");
        Preconditions.checkNotNull(symbol, "Symbol cannot be null");
        return historicalDataHelper.getKryptoHistoricalData(symbol, startDate, endDate);
    }

    @Override
    public KryptoInfosDTO getAllKryptoInfos() {
        return kryptoHelper.getKryptoInfos();
    }

    @Override
    public SymbolsDTO getSymbolLegend() {
        return kryptoHelper.getSymbolLegend();
    }
}
