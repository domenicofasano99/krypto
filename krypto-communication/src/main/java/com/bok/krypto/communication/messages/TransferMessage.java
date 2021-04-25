package com.bok.krypto.communication.messages;

import com.bok.krypto.communication.AbstractMessage;

import java.math.BigDecimal;

public class TransferMessage extends AbstractMessage {
    public String symbol;
    public String destination;
    public BigDecimal amount;
    public Long transferId;
}
