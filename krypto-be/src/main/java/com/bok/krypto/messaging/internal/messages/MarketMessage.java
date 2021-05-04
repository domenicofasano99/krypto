package com.bok.krypto.messaging.internal.messages;

import com.bok.krypto.messaging.internal.AbstractMessage;

import java.math.BigDecimal;

public abstract class MarketMessage extends AbstractMessage {
    public Long transactionId;
    public BigDecimal amount;
    public String symbol;
}
