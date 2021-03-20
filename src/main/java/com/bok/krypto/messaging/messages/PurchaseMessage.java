package com.bok.krypto.messaging.messages;

import java.math.BigDecimal;

public class PurchaseMessage extends MarketMessage {
    public Long transactionId;
    public String symbol;
    public BigDecimal amount;
}
