package com.bok.krypto.messaging.messages;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SellMessage extends MarketMessage {
    public Long transactionId;
    public BigDecimal amount;
    public String symbol;
}
