package com.bok.krypto.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

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

    @OneToMany(cascade = {CascadeType.ALL})
    Set<HistoricalData> historicalData = new HashSet<>();

    public Krypto() {
        //hibernate
    }

    public Krypto(String name, String symbol, BigDecimal price) {
        this.name = name;
        this.symbol = symbol;
        this.price = price;
    }

    public Set<HistoricalData> getHistoricalData() {
        return historicalData;
    }

    public void setHistoricalData(Set<HistoricalData> historicalDataSet) {
        this.historicalData = historicalDataSet;
    }

    public void addHistoricalData(HistoricalData historicalData) {
        this.historicalData.add(historicalData);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getNetworkFee() {
        return networkFee;
    }

    public void setNetworkFee(Double networkFee) {
        this.networkFee = networkFee;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String code) {
        this.symbol = code;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("networkFee", networkFee)
                .toString();
    }
}
