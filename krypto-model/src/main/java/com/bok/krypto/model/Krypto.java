package com.bok.krypto.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Krypto {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Column(unique = true)
    private String symbol;

    @Column
    private Double networkFee;

    @Column
    private BigDecimal price;

    @Column
    @UpdateTimestamp
    private Instant updateTimestamp;

    @OneToMany
    private List<HistoricalData> historicalData = new ArrayList<>();

    @OneToMany
    private List<Wallet> wallets = new ArrayList<>();


    public Krypto(String name, String symbol, BigDecimal price) {
        this.name = name;
        this.symbol = symbol;
        this.price = price;
    }

    public Krypto(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public void addHistoricalData(HistoricalData dataRecord) {
        this.historicalData.add(dataRecord);
    }
}
