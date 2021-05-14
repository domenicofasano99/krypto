package com.bok.integration.krypto.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferRequestDTO {

    public String symbol;
    public String source;
    public String destination;
    public BigDecimal amount;

    public TransferRequestDTO() {
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("symbol", symbol)
                .append("source", source)
                .append("destination", destination)
                .append("amount", amount)
                .toString();
    }
}
