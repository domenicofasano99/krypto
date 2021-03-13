package com.bok.krypto.controller;

import com.bok.krypto.dto.*;
import com.bok.krypto.service.interfaces.KryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KryptoController {

    @Autowired
    KryptoService kryptoService;

    @GetMapping("/prices")
    PricesResponseDTO getKryptoPrices(PricesRequestDTO requestDTO) {
        return kryptoService.getKryptoPrices(requestDTO);
    }

    @GetMapping("/price")
    PriceResponseDTO getKryptoPrice(PriceRequestDTO priceRequestDTO) {
        return kryptoService.getKryptoPrice(priceRequestDTO);
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
