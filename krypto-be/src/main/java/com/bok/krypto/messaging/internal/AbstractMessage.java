package com.bok.krypto.messaging.internal;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public abstract class AbstractMessage implements Serializable {

    public Long accountId;

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("userId", accountId)
                .toString();
    }
}
