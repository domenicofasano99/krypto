package com.bok.krypto.integration.internal.dto;

import com.google.type.Money;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class WalletInfoDTO {
    public BigDecimal availableAmount;
    public String symbol;
    public String address;
    public Money value;
    public Instant creationTimestamp;
    public Instant updateTimestamp;
    public UUID id;
    public List<WalletBalanceInfoDTO> balanceHistory;
    public List<ActivityDTO> activities;
}
