package com.bok.krypto.integration.internal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class TransferInfoDTO {
    public UUID publicId;
    public String type;
    public BigDecimal amount;
    public String source;
    public String destination;
    public Instant timestamp;
    public String status;
}
