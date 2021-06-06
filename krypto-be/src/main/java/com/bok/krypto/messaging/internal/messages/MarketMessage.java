package com.bok.krypto.messaging.internal.messages;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class MarketMessage implements Serializable {
    public Long accountId;
    public Long transactionId;
    public BigDecimal amount;
    public String symbol;
}
