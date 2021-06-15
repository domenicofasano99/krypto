package com.bok.krypto.messaging.messages;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WalletCreationMessage implements Serializable {
    public String symbol;
    public Long id;
}
