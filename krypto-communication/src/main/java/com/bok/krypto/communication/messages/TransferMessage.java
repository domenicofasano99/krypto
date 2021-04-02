package com.bok.krypto.communication.messages;

import com.bok.krypto.communication.AbstractMessage;

import java.math.BigDecimal;
import java.util.UUID;

public class TransferMessage extends AbstractMessage {
    public String symbol;
    public UUID destination;
    public BigDecimal amount;
    public Long transferId;
}
