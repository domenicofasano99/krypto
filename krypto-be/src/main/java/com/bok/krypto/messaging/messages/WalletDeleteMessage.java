package com.bok.krypto.messaging.messages;

import java.io.Serializable;

public class WalletDeleteMessage implements Serializable {
    public Long accountId;
    public String symbol;
}
