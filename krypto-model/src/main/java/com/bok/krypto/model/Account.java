package com.bok.krypto.model;

import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
@ToString
@Entity
public class Account {

    @Id
    @Column
    private Long id;

    @OneToMany
    @ToString.Exclude
    private Set<Wallet> wallets;

    @OneToMany
    @ToString.Exclude
    private Set<Transaction> transactions;

    @OneToMany
    @ToString.Exclude
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equal(id, account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
