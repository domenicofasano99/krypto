package com.bok.krypto.messaging.internal.messages;

import java.math.BigDecimal;

public class SellMessage extends MarketMessage {
    public Long transactionId;
    public BigDecimal amount;
    public String symbol;
}
