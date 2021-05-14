package com.bok.krypto.service;

import com.bok.krypto.integration.internal.dto.PurchaseRequestDTO;
import com.bok.krypto.integration.internal.dto.HistoricalDataDTO;
import com.bok.krypto.integration.internal.dto.HistoricalDataRequestDTO;
import com.bok.krypto.integration.internal.dto.KryptoInfoDTO;
import com.bok.krypto.integration.internal.dto.KryptoInfosDTO;
import com.bok.krypto.integration.internal.dto.KryptoInfosRequestDTO;
import com.bok.krypto.integration.internal.dto.PriceResponseDTO;
import com.bok.krypto.integration.internal.dto.PricesRequestDTO;
import com.bok.krypto.integration.internal.dto.PricesResponseDTO;
import com.bok.krypto.integration.internal.dto.SellRequestDTO;
import com.bok.krypto.integration.internal.dto.TransactionDTO;
import com.bok.krypto.helper.AccountHelper;
import com.bok.krypto.helper.HistoricalDataHelper;
import com.bok.krypto.helper.KryptoHelper;
import com.bok.krypto.helper.MarketHelper;
import com.bok.krypto.helper.WalletHelper;
import com.bok.krypto.service.interfaces.MarketService;
import com.google.common.base.Preconditions;
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
    public TransactionDTO buy(Long accountId, PurchaseRequestDTO purchaseRequestDTO) {
        Preconditions.checkNotNull(accountId);
        Preconditions.checkNotNull(purchaseRequestDTO, "Request body was empty");
        Preconditions.checkNotNull(purchaseRequestDTO.symbol, "Symbol cannot be null");
        Preconditions.checkNotNull(purchaseRequestDTO.amount, "Amount cannot be null");
        return marketHelper.buy(accountId, purchaseRequestDTO);
    }

    @Override
    public TransactionDTO sell(Long accountId, SellRequestDTO sellRequestDTO) {
        Preconditions.checkNotNull(accountId);
        Preconditions.checkNotNull(sellRequestDTO, "Request body was empty");
        Preconditions.checkNotNull(sellRequestDTO.symbol, "Symbol cannot be null");
        Preconditions.checkNotNull(sellRequestDTO.amount, "Amount cannot be null");
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

    @Override
    public HistoricalDataDTO getKryptoHistoricalData(HistoricalDataRequestDTO requestDTO) {
        Preconditions.checkNotNull(requestDTO.start, "Start date Instant cannot be null");
        Preconditions.checkNotNull(requestDTO.end, "End date Instant cannot be null");
        Preconditions.checkNotNull(requestDTO.symbol, "Symbol cannot be null");
        return historicalDataHelper.getKryptoHistoricalData(requestDTO.symbol, requestDTO.start, requestDTO.end);
    }
}
