package com.bok.krypto.integration.internal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferResponseDTO {
    public String status;
    public String publicId;
    public BigDecimal amount;
    public UUID source;
    public UUID destination;

    public TransferResponseDTO() {
    }
}
