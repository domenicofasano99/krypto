package com.bok.krypto.messaging.messages;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransferMessage implements Serializable {
    public Long accountId;
    public String symbol;
    public String destination;
    public BigDecimal amount;
    public Long transferId;


}
