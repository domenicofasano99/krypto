package com.bok.krypto.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.common.aliasing.qual.Unique;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Account {

    @Id
    @Column
    private Long id;

    @Column
    @Unique
    private String email;

    @OneToMany
    private Set<Wallet> wallets;

    @OneToMany
    private Set<Transaction> transactions;

    @OneToMany
    private Set<Transfer> transfers;

    @CreationTimestamp
    private Instant creationTimestamp;

    @UpdateTimestamp
    private Instant updateTimestamp;


    public Account(Long id) {
        this.id = id;
    }

    public Account(Set<Wallet> wallets, Set<Transaction> transactions) {
        this.wallets = wallets;
        this.transactions = transactions;
    }
}
