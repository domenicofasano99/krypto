package com.bok.krypto.messaging.internal.messages;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WalletMessage implements Serializable {
    public String symbol;
    public Long id;
}
