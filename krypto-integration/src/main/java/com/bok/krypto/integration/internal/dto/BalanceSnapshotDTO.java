package com.bok.krypto.integration.internal.dto;

import java.math.BigDecimal;
import java.time.Instant;

public class BalanceSnapshotDTO {
    public Instant instant;
    public BigDecimal value;
    public BigDecimal amount;
}
