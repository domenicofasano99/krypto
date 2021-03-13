package com.bok.krypto.service.interfaces;


import java.math.BigDecimal;
import java.util.UUID;

public interface ExchangeService {

    void transfer(UUID from, UUID to, BigDecimal amount);
}
