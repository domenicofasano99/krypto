package com.bok.krypto.integration.internal.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class WalletInfoDTO {
    public BigDecimal availableAmount;
    public String symbol;
    public Instant creationTimestamp;
    public Instant updateTimestamp;
    public UUID id;
    public List<WalletBalanceInfoDTO> balanceHistory;
    public List<TransactionDTO> transactions;
}
