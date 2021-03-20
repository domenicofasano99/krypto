package com.bok.krypto.controller;

import com.bok.integration.krypto.PurchaseRequestDTO;
import com.bok.integration.krypto.dto.SellRequestDTO;
import com.bok.integration.krypto.dto.TransactionDTO;
import com.bok.krypto.service.interfaces.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("market")
public class MarketController {

    @Autowired
    MarketService marketService;

    @PostMapping("/{userId}/buy")
    public TransactionDTO buy(@PathVariable("userId") Long userId, PurchaseRequestDTO purchaseRequestDTO) {
        return marketService.buy(userId, purchaseRequestDTO);

    }

    @PostMapping("/{userId}/sell")
    public TransactionDTO sell(@PathVariable("userId") Long userId, SellRequestDTO sellRequestDTO) {
        return marketService.sell(userId, sellRequestDTO);
    }

}
