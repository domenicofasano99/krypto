package com.bok.krypto.integration.internal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDTO {
    public String publicId;
    public Long accountId;
    public String type;
    public BigDecimal amount;
    private String status;

    public TransactionDTO(String publicId, String status, String type, BigDecimal amount) {
        this.publicId = publicId;
        this.status = status;
        this.type = type;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("publicId", publicId)
                .append("accountId", accountId)
                .append("type", type)
                .append("amount", amount)
                .append("status", status)
                .toString();
    }
}
