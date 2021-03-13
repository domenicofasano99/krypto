package com.bok.krypto.service.interfaces;

import com.bok.integration.krypto.dto.TransactionDTO;

import java.math.BigDecimal;

public interface MarketService {

    TransactionDTO buy(Long userId, String symbol, BigDecimal amount);

    TransactionDTO sell(Long userId, String symbol, BigDecimal amount);
}
