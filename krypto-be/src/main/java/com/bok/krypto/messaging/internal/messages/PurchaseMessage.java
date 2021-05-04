package com.bok.krypto.messaging.internal.messages;

import java.math.BigDecimal;

public class PurchaseMessage extends MarketMessage {
    public Long accountId;
    public Long transactionId;
    public String symbol;
    public BigDecimal amount;

    public PurchaseMessage(){

    }

    public PurchaseMessage(Long accountId, Long activityId, BigDecimal amount, String symbol) {
        this.transactionId = accountId;
        this.accountId = accountId;
        this.symbol = symbol;
        this.amount = amount;
    }
}
