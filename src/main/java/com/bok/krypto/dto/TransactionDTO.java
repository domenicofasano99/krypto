package com.bok.krypto.dto;

import com.bok.krypto.model.Transaction;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;

public class TransactionDTO {
    public Long userId;
    public Transaction.Type type;
    public BigDecimal amount;

    public TransactionDTO() {
    }

    public TransactionDTO(Long userId, Transaction.Type type, BigDecimal amount) {
        this.userId = userId;
        this.type = type;
        this.amount = amount;
    }

    public static TransactionDTO of(Transaction t) {
        return new TransactionDTO(t.getUser().getId(), t.getType(), t.getAmount());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("userId", userId)
                .append("type", type)
                .append("amount", amount)
                .toString();
    }
}
