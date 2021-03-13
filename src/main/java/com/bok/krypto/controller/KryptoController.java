package com.bok.krypto.controller;

import com.bok.krypto.dto.*;
import com.bok.krypto.service.interfaces.KryptoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class KryptoController {

    @Autowired
    KryptoService kryptoService;

    @GetMapping("/prices")
    PricesResponseDTO getKryptoPrices(PricesRequestDTO requestDTO) {
        return kryptoService.getKryptoPrices(requestDTO);
    }

    @GetMapping("/price/{symbol}")
    PriceResponseDTO getKryptoPrice(@PathVariable("symbol") String symbol) {
        log.info("received request for {} price", symbol);
        return kryptoService.getKryptoPrice(symbol);
    }

    @GetMapping("/info")
    KryptoInfoDTO getKryptoInfo(KryptoInfoRequestDTO requestDTO) {
        return kryptoService.getKryptoInfo(requestDTO);
    }

    @GetMapping("/infos")
    KryptoInfosDTO getKryptoInfos(KryptoInfosRequestDTO requestDTO) {
        return kryptoService.getKryptoInfos(requestDTO);

    }
}
