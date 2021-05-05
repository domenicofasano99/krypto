package com.bok.krypto.messaging.internal.messages;

import com.bok.krypto.messaging.internal.AbstractMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class MarketMessage extends AbstractMessage {
    public Long transactionId;
    public BigDecimal amount;
    public String symbol;
}
