package com.bok.krypto.integration.internal.dto;


import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class WalletInfoDTO {
    public BigDecimal availableAmount;
    public String symbol;
    public String address;
    public Instant creationTimestamp;
    public Instant updateTimestamp;
    public List<BalanceSnapshotDTO> balanceHistory;
    public List<ActivityDTO> activities;
    public Status status;

    public enum Status {
        PENDING,
        CREATED
    }
}
