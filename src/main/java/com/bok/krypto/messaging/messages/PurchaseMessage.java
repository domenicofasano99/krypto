package com.bok.krypto.messaging.messages;

import com.bok.krypto.messaging.AbstractMessage;

import java.math.BigDecimal;

public class PurchaseMessage extends MarketMessage {
    public Long transactionId;
    public String symbol;
    public BigDecimal amount;
}
