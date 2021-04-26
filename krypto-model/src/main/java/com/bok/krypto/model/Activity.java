package com.bok.krypto.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@DiscriminatorColumn(name = "transaction_type",
        discriminatorType = DiscriminatorType.STRING)
public abstract class Activity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 36, unique = true, updatable = false)
    private String publicId;

    @Column(updatable = false)
    @CreationTimestamp
    private Instant creationTimestamp;

    @Column
    @UpdateTimestamp
    private Instant updateTimestamp;

    @ManyToOne
    private Account account;

    @Column(updatable = false)
    private BigDecimal amount;

    @Column
    private Double fee;

    @Column(updatable = false)
    @Enumerated(EnumType.STRING)
    public Transaction.Status status;

    @PrePersist
    public void prePersist() {
        this.status = Status.PENDING;
        this.publicId = UUID.randomUUID().toString();
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public Instant getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Instant creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public Instant getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Instant updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
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

    public enum Status {
        PENDING,
        SETTLED,
        REJECTED
    }
}
