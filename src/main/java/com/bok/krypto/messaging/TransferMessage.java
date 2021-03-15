package com.bok.krypto.messaging;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public class TransferMessage implements Serializable {
    public Long userId;
    public String symbol;
    public UUID destination;
    public BigDecimal amount;
}
