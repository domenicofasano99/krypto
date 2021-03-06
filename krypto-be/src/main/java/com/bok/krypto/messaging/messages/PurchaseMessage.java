package com.bok.krypto.messaging.messages;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseMessage extends MarketMessage {
    public Long accountId;
    public Long transactionId;
    public String kryptoSymbol;
    public BigDecimal moneyAmount;

}
