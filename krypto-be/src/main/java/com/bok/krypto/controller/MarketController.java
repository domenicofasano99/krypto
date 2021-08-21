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
import com.bok.krypto.integration.internal.dto.ActivityDTO;
import com.bok.krypto.service.interfaces.MarketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@Slf4j
@RestController
@RequestMapping("/market")
public class MarketController {

    @Autowired
    MarketService marketService;

    @PostMapping("/buy")
    public ActivityDTO buy(@RequestParam("accountId") Long userId, @RequestBody PurchaseRequestDTO purchaseRequestDTO) {
        return marketService.buy(userId, purchaseRequestDTO);

    }

    @PostMapping("/sell")
    public ActivityDTO sell(@RequestParam("accountId") Long userId, @RequestBody SellRequestDTO sellRequestDTO) {
        return marketService.sell(userId, sellRequestDTO);
    }

    @GetMapping("/prices")
    public PricesResponseDTO getKryptoPrices(@RequestBody PricesRequestDTO requestDTO) {
        return marketService.getKryptoPrices(requestDTO);
    }

    @GetMapping("/{symbol}/price")
    public PriceResponseDTO getKryptoPrice(@PathVariable("symbol") String symbol) {
        return marketService.getKryptoPrice(symbol);
    }

    @GetMapping("/{symbol}/info")
    public KryptoInfoDTO getKryptoInfo(@PathVariable("symbol") String symbol) {
        return marketService.getKryptoInfo(symbol);
    }

    @GetMapping("/infos")
    public KryptoInfosDTO getKryptoInfos(@RequestBody KryptoInfosRequestDTO requestDTO) {
        return marketService.getKryptoInfos(requestDTO);
    }

    @GetMapping("/{symbol}/history")
    public HistoricalDataDTO getKryptoHistoricalData(@PathVariable("symbol") String symbol, @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate, @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate) {
        log.info("{} history from {} to {}", symbol, startDate, endDate);
        return marketService.getKryptoHistoricalData(symbol, startDate, endDate);
    }

    @GetMapping("/list")
    public KryptoInfosDTO getKryptoInfoList() {
        return marketService.getAllKryptoInfos();
    }

    @GetMapping("/legend")
    public SymbolsDTO getKryptoSymbols() {
        return marketService.getSymbolLegend();
    }

}
