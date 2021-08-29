package com.bok.krypto.messaging.messages;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SellMessage extends MarketMessage {
    public Long transactionId;
}
