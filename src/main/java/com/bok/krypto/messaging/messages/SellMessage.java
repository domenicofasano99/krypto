package com.bok.krypto.messaging.messages;

import com.bok.krypto.messaging.AbstractMessage;

import java.math.BigDecimal;

public class SellMessage extends AbstractMessage {
    public Long transactionId;
    public BigDecimal amount;
    public String symbol;
}
