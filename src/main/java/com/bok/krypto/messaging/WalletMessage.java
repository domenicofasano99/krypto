package com.bok.krypto.messaging;

import java.io.Serializable;
import java.util.UUID;

public class WalletMessage implements Serializable {
    public Long userId;
    public String symbol;

    public UUID id;
}
