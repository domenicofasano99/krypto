package com.bok.krypto.dto;

import com.bok.krypto.model.Krypto;

import java.math.BigDecimal;
import java.time.Instant;

public class KryptoInfoDTO {

    public String name;

    public String symbol;

    public Double networkFee;

    public BigDecimal price;

    public Instant updateTimestamp;

    public KryptoInfoDTO() {
    }

    public KryptoInfoDTO(String name, String symbol, Double networkFee, BigDecimal price, Instant updateTimestamp) {
        this.name = name;
        this.symbol = symbol;
        this.networkFee = networkFee;
        this.price = price;
        this.updateTimestamp = updateTimestamp;
    }

    public static KryptoInfoDTO of(Krypto krypto) {
        KryptoInfoDTO dto = new KryptoInfoDTO();
        dto.name = krypto.getName();
        dto.price = krypto.getPrice();
        dto.symbol = krypto.getSymbol();
        dto.updateTimestamp = krypto.getUpdateTimestamp();
        dto.networkFee = krypto.getNetworkFee();
        return dto;
    }
}
