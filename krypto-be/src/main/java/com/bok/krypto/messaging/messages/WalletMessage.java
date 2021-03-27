package com.bok.krypto.messaging.messages;

import com.bok.krypto.messaging.AbstractMessage;

import java.util.UUID;

public class WalletMessage extends AbstractMessage {
    public String symbol;

    public UUID id;
}
