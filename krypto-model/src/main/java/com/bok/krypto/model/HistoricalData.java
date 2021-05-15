package com.bok.krypto.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Entity
public class HistoricalData {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Krypto krypto;

    @Column
    private Double circulatingSupply;

    @Column
    private Double totalSupply;

    @Column
    private Integer cmcRank;

    @Column
    private String lastUpdated;

    @CreationTimestamp
    private Instant recordTimestamp;

    @Column
    private Double price;

    @Column
    private Double volume24h;

    @Column
    private Double percentChange1h;

    @Column
    private Double percentChange24h;

    @Column
    private Double percentChange7d;

    @Column
    private Double percentChange30d;

    @Column
    private Double marketCap;


    public HistoricalData(Krypto krypto, Double price) {
        this.krypto = krypto;
        this.price = price;
    }
}
