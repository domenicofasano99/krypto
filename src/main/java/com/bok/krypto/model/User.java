package com.bok.krypto.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.checkerframework.common.aliasing.qual.Unique;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.Set;

@Entity
public class User {

    @Id
    @Column
    private Long id;

    @Column
    @Unique
    private String email;

    @OneToMany(mappedBy = "id", cascade = {CascadeType.REMOVE})
    private Set<Wallet> wallets;

    @OneToMany(mappedBy = "id", cascade = {CascadeType.REMOVE})
    private Set<Transaction> transactions;

    @CreationTimestamp
    private Instant creationTimestamp;

    @UpdateTimestamp
    private Instant updateTimestamp;

    public User() {
        //hibernate
    }

    public User(Long id) {
        this.id = id;
    }

    public User(Set<Wallet> wallets, Set<Transaction> transactions) {
        this.wallets = wallets;
        this.transactions = transactions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Set<Wallet> getWallets() {
        return wallets;
    }

    public void setWallets(Set<Wallet> wallets) {
        this.wallets = wallets;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("wallets", wallets)
                .append("transactions", transactions)
                .toString();
    }
}
