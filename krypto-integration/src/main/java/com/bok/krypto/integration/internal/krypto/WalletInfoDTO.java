package com.bok.integration.krypto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class WalletInfoDTO {
    public BigDecimal availableAmount;
    public String symbol;
    public Instant creationTimestamp;
    public Instant updateTimestamp;
    public UUID id;
}
