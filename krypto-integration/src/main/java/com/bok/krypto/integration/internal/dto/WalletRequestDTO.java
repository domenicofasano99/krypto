package com.bok.krypto.integration.internal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WalletRequestDTO {
    public String symbol;
}
