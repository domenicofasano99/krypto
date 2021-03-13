package com.bok.krypto.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("symbol", symbol)
                .append("price", price)
                .toString();
    }
}
