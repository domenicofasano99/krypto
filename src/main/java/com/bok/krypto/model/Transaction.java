package com.bok.krypto.model;

import com.google.common.base.Objects;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
public class Transaction {

    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private Long id;

    @ManyToOne
    private Wallet sourceWallet;

    @ManyToOne
    private Wallet destinationWallet;

    @Column
    @CreationTimestamp
    private Instant timestamp;

    @Column
    private BigDecimal amount;

    @Column
    @Enumerated(EnumType.STRING)
    public Type type;

    @Column
    @Enumerated(EnumType.STRING)
    public Status status;

    @ManyToOne
    private User user;

    public Transaction(User user, Type type, Status status, Wallet sourceWallet, Wallet destinationWallet, BigDecimal amount) {
        this.type = type;
        this.user = user;
        this.status = status;
        this.sourceWallet = sourceWallet;
        this.destinationWallet = destinationWallet;
        this.amount = amount;
    }

    public Transaction() {

    }

    public Transaction(Type type, Status status) {
        this.type=type;
        this.status = status;
    }

    public enum Type {
        BUY,
        SELL,
        TRANSFER
    }

    public enum Status {
        PENDING,
        SETTLED,
        REJECTED
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equal(id, that.id) && Objects.equal(sourceWallet, that.sourceWallet) && Objects.equal(destinationWallet, that.destinationWallet) && Objects.equal(timestamp, that.timestamp) && Objects.equal(amount, that.amount) && type == that.type && status == that.status && Objects.equal(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, sourceWallet, destinationWallet, timestamp, amount, type, status, user);
    }
}
