package com.bok.krypto.controller;

import com.bok.krypto.dto.PriceRequestDTO;
import com.bok.krypto.dto.PriceResponseDTO;
import com.bok.krypto.dto.PricesRequestDTO;
import com.bok.krypto.dto.PricesResponseDTO;
import com.bok.krypto.service.interfaces.KryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/krypto")
public class KryptoController {

    @Autowired
    KryptoService kryptoService;

    @GetMapping("/prices")
    PricesResponseDTO getKryptoPrices(PricesRequestDTO requestDTO) {
        return kryptoService.getKryptoPrices(requestDTO);
    }

    @GetMapping("/price")
    PriceResponseDTO getKryptoPrice(PriceRequestDTO priceRequestDTO){
        return kryptoService.getKryptoPrice(priceRequestDTO);
    }
}
