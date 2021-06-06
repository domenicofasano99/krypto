package com.bok.krypto.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Column
    @CreationTimestamp
    private Instant creationTime;

    @Column
    @UpdateTimestamp
    private Instant updateTime;

    @Column
    @Enumerated(value = EnumType.STRING)
    private Status status;


    public Wallet(Account u, Krypto k) {
        this.account = u;
        this.krypto = k;
    }

    @PrePersist
    public void prePersist() {
        this.status = Status.PENDING;
    }

    public enum Status {
        PENDING,
        CREATED,
        FAILED
    }
}
