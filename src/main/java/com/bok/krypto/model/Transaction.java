package com.bok.krypto.model;

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    private Wallet sourceWallet;

    @ManyToOne
    private Wallet destinationWallet;

    @CreationTimestamp
    private Instant timestamp;

    @Column
    private BigDecimal amount;

    @Column
    @Enumerated(EnumType.STRING)
    private Type type;

    public Transaction(Type type, Wallet sourceWallet, Wallet destinationWallet, BigDecimal amount) {
        this.type = type;
        this.sourceWallet = sourceWallet;
        this.destinationWallet = destinationWallet;
        this.amount = amount;
    }

    public static enum Type {
        BUY,
        SELL,
        TRANSFER
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Wallet getSourceWallet() {
        return sourceWallet;
    }

    public void setSourceWallet(Wallet sourceWallet) {
        this.sourceWallet = sourceWallet;
    }

    public Wallet getDestinationWallet() {
        return destinationWallet;
    }

    public void setDestinationWallet(Wallet destinationWallet) {
        this.destinationWallet = destinationWallet;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("sourceWallet", sourceWallet)
                .append("destinationWallet", destinationWallet)
                .append("timestamp", timestamp)
                .append("amount", amount)
                .append("type", type)
                .toString();
    }
}
