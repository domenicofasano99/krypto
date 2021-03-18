package com.bok.krypto.messaging.messages;

import java.math.BigDecimal;

public class SellMessage extends MarketMessage {
    public Long transactionId;
    public BigDecimal amount;
    public String symbol;
}
