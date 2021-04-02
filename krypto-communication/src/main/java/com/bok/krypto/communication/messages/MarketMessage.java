package com.bok.krypto.communication.messages;

import com.bok.krypto.communication.AbstractMessage;

import java.math.BigDecimal;

public abstract class MarketMessage extends AbstractMessage {
    public Long transactionId;
    public BigDecimal amount;
    public String symbol;
}
