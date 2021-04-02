package com.bok.krypto.communication.messages;

import java.math.BigDecimal;

public class PurchaseMessage extends MarketMessage {
    public Long transactionId;
    public String symbol;
    public BigDecimal amount;
}
