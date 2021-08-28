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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Wallet {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String address;

    @ManyToOne
    private Account account;

    @OneToOne
    private Krypto krypto;

    @Column
    private BigDecimal availableAmount;

    @CreationTimestamp
    private Instant creationTime;

    @UpdateTimestamp
    private Instant updateTime;

    @Column
    @Enumerated(value = EnumType.STRING)
    private Status status;

    @OneToMany
    @ToString.Exclude
    private List<Transfer> transfers;

    @OneToMany
    @ToString.Exclude
    private List<Transaction> transactions;

    @OneToMany
    @ToString.Exclude
    private List<BalanceSnapshot> balanceSnapshot;

    @Column
    private boolean deleted;


    public Wallet(Account u, Krypto k) {
        this.account = u;
        this.krypto = k;
    }

    @PrePersist
    public void prePersist() {
        this.status = Status.PENDING;
    }

    public BalanceSnapshot createSnapshot() {
        BalanceSnapshot bh = new BalanceSnapshot();
        bh.setWallet(this);
        bh.setAmount(this.availableAmount);
        bh.setValue(this.availableAmount.multiply(this.krypto.getPrice()));
        return bh;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return Objects.equal(address, wallet.address);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(address);
    }

    public enum Status {
        PENDING,
        CREATED
    }
}
