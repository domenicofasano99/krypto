package com.bok.krypto.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class BalanceSnapshot {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Wallet wallet;

    @Column
    @CreationTimestamp
    private Instant timestamp;

    @Column
    private BigDecimal value;

    @Column
    private BigDecimal amount;
}
