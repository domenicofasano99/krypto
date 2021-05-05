package com.bok.krypto.messaging.internal.messages;

import com.bok.krypto.messaging.internal.AbstractMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransferMessage extends AbstractMessage {
    public String symbol;
    public String destination;
    public BigDecimal amount;
    public Long transferId;


}
