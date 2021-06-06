package com.bok.krypto.controller;

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
import com.bok.krypto.integration.internal.dto.TransactionDTO;
import com.bok.krypto.service.interfaces.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;

@RestController
@RequestMapping("/market")
public class MarketController {

    @Autowired
    MarketService marketService;

    @PostMapping("/buy")
    public TransactionDTO buy(@RequestParam("accountId") Long userId, @RequestBody PurchaseRequestDTO purchaseRequestDTO) {
        return marketService.buy(userId, purchaseRequestDTO);

    }

    @PostMapping("/sell")
    public TransactionDTO sell(@RequestParam("accountId") Long userId, @RequestBody SellRequestDTO sellRequestDTO) {
        return marketService.sell(userId, sellRequestDTO);
    }

    @GetMapping("/prices")
    PricesResponseDTO getKryptoPrices(@RequestBody PricesRequestDTO requestDTO) {
        return marketService.getKryptoPrices(requestDTO);
    }

    @GetMapping("/{symbol}/price")
    PriceResponseDTO getKryptoPrice(@PathVariable("symbol") String symbol) {
        return marketService.getKryptoPrice(symbol);
    }

    @GetMapping("/{symbol}/info")
    KryptoInfoDTO getKryptoInfo(@PathVariable("symbol") String symbol) {
        return marketService.getKryptoInfo(symbol);
    }

    @GetMapping("/infos")
    KryptoInfosDTO getKryptoInfos(@RequestBody KryptoInfosRequestDTO requestDTO) {
        return marketService.getKryptoInfos(requestDTO);
    }

    @GetMapping("/{symbol}/history")
    HistoricalDataDTO getKryptoHistoricalData(@PathVariable("symbol") String symbol, @RequestParam("startDate") Instant startInstant, @RequestParam("endDate") Instant endInstant) {
        return marketService.getKryptoHistoricalData(symbol, startInstant, endInstant);
    }

    @GetMapping("/list")
    KryptoInfosDTO getKryptoInfoList() {
        return marketService.getAllKryptoInfos();
    }

    @GetMapping("/legend")
    SymbolsDTO getKryptoSymbols() {
        return marketService.getSymbolLegend();
    }

}
