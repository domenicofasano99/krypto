package com.bok.krypto.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
public class Wallet {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    private Account account;

    @OneToOne
    private Krypto krypto;

    @Column
    private BigDecimal availableAmount;

    @Column
    @CreationTimestamp
    private Instant creationTime;

    @Column
    @UpdateTimestamp
    private Instant updateTime;

    @Column
    @Enumerated(value = EnumType.STRING)
    private Status status;

    public Wallet() {
        //hibernate
    }


    public Wallet(Account u, Krypto k) {
        this.account = u;
        this.krypto = k;
    }

    @PrePersist
    public void prePersist() {
        this.status = Status.PENDING;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Krypto getKrypto() {
        return krypto;
    }

    public void setKrypto(Krypto krypto) {
        this.krypto = krypto;
    }

    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(BigDecimal availableAmount) {
        this.availableAmount = availableAmount;
    }

    public Instant getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Instant creationTime) {
        this.creationTime = creationTime;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public Account getUser() {
        return account;
    }

    public void setUser(Account account) {
        this.account = account;
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
                .append("krypto", krypto)
                .append("availableAmount", availableAmount)
                .append("creationTime", creationTime)
                .append("updateTime", updateTime)
                .toString();
    }

    public enum Status {
        PENDING,
        CREATED,
        FAILED
    }
}
