package com.bok.krypto.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@MappedSuperclass
@DiscriminatorColumn(name = "transaction_type",
        discriminatorType = DiscriminatorType.STRING)
public abstract class Activity {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    @CreationTimestamp
    private Instant creationTimestamp;

    @Column
    @UpdateTimestamp
    private Instant updateTimestamp;

    @ManyToOne
    private User user;

    @Column(updatable = false)
    private BigDecimal amount;

    @Column
    private Double fee;

    @Column(updatable = false)
    @Enumerated(EnumType.STRING)
    public Transaction.Status status;

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

    public Instant getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Instant timestamp) {
        this.creationTimestamp = timestamp;
    }

    public Instant getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Instant updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public Transaction.Status getStatus() {
        return status;
    }

    public void setStatus(Transaction.Status status) {
        this.status = status;
    }
}