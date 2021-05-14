package com.bok.integration.krypto.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class TransferInfoDTO {
    public String publicId;
    public String type;
    public BigDecimal amount;
    public String source;
    public String destination;
    public Instant timestamp;
    public String status;
}
