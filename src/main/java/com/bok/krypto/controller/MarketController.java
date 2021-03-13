package com.bok.krypto.controller;

import com.bok.integration.krypto.dto.TransactionDTO;
import com.bok.integration.krypto.dto.TransactionRequestDTO;
import com.bok.krypto.service.interfaces.MarketService;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("market")
public class MarketController {

    @Autowired
    MarketService marketService;

    @PostMapping("buy")
    public TransactionDTO buy(TransactionRequestDTO request) {
        Preconditions.checkNotNull(request);
        Preconditions.checkNotNull(request.userId);
        Preconditions.checkNotNull(request.symbol);
        Preconditions.checkNotNull(request.amount);
        return marketService.buy(request.userId, request.symbol, request.amount);

    }

    @PostMapping("sell")
    public TransactionDTO sell(TransactionRequestDTO request) {
        Preconditions.checkNotNull(request);
        Preconditions.checkNotNull(request.userId);
        Preconditions.checkNotNull(request.symbol);
        Preconditions.checkNotNull(request.amount);
        return marketService.sell(request.userId, request.symbol, request.amount);
    }

}
