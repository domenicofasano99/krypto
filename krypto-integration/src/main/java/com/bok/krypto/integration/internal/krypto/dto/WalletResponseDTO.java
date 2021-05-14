package com.bok.integration.krypto.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.ToString;

@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class WalletResponseDTO {

    public Status status;

    public WalletResponseDTO(Status status) {
        this.status = status;
    }

    public WalletResponseDTO() {
    }

    public enum Status {
        ACCEPTED,
        DENIED
    }
}
