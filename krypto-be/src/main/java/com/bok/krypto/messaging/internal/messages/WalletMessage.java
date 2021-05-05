package com.bok.krypto.messaging.internal.messages;

import com.bok.krypto.messaging.internal.AbstractMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WalletMessage extends AbstractMessage {
    public String symbol;
    public Long id;
}
