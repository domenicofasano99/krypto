package com.bok.krypto.dto;

import java.math.BigDecimal;

public class PriceResponseDTO {

    public String symbol;
    public BigDecimal price;

    public PriceResponseDTO(String symbol, BigDecimal price) {
        this.symbol = symbol;
        this.price = price;
    }

    public static PriceResponseDTO of(String symbol, BigDecimal price) {
        return new PriceResponseDTO(symbol, price);
    }
}
