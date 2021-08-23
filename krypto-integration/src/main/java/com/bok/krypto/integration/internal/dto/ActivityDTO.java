package com.bok.krypto.integration.internal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivityDTO {

    public UUID publicId;
    public Long accountId;
    public Type type;
    public BigDecimal amount;
    public String status;

    public ActivityDTO(UUID publicId, Long accountId, Type type, BigDecimal amount, String status) {
        this.publicId = publicId;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.status = status;
    }

    public enum Type {
        PURCHASE,
        SELL,
        TRANSFER
    }

}
