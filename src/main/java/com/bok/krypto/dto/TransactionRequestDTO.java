package com.bok.krypto.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionRequestDTO {

    public Long userId;
    public String symbol;
    public BigDecimal amount;


}
