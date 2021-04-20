package com.bok.krypto.controller;

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
import com.bok.krypto.service.interfaces.MarketService;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/market")
public class MarketController {

    @Autowired
    MarketService marketService;

    @PostMapping("/buy")
    public TransactionDTO buy(@RequestParam("userId") Long userId, @RequestBody PurchaseRequestDTO purchaseRequestDTO) {
        return marketService.buy(userId, purchaseRequestDTO);

    }

    @PostMapping("/sell")
    public TransactionDTO sell(@RequestParam("userId") Long userId, @RequestBody SellRequestDTO sellRequestDTO) {
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

    @GetMapping("/history")
    HistoricalDataDTO getKryptoHistoricalData(@RequestBody HistoricalDataRequestDTO requestDTO) {
        return marketService.getKryptoHistoricalData(requestDTO);
    }

}
