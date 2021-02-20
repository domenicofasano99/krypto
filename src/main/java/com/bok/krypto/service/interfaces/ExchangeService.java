package com.bok.krypto.service.interfaces;


import java.math.BigDecimal;
import java.util.UUID;

public interface ExchangeService {

    void buy(BigDecimal amount, String kryptoCode, UUID walletId);

    void sell(BigDecimal amount, String kryptoCode, UUID walletId);

    void transfer(UUID from, UUID to, BigDecimal amount);
}
