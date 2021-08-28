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
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class HistoricalData {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Krypto krypto;

    @CreationTimestamp
    private Instant timestamp;

    @Column
    private Double price;


    public HistoricalData(Krypto krypto, Double price) {
        this.krypto = krypto;
        this.price = price;
    }

    public HistoricalData(Krypto krypto, Double price, Instant timestamp) {
        this(krypto, price);
        this.timestamp = timestamp;
    }
}
