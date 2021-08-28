package com.bok.krypto.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
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
