package com.bok.krypto.messaging.internal.messages;

import com.bok.krypto.messaging.internal.AbstractMessage;

import java.math.BigDecimal;

public class TransferMessage extends AbstractMessage {
    public String symbol;
    public String destination;
    public BigDecimal amount;
    public Long transferId;
}
