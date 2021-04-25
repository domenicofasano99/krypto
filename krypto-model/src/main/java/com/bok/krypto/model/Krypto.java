package com.bok.krypto.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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

    public Krypto() {
        //hibernate
    }

    public Krypto(String name, String symbol, BigDecimal price) {
        this.name = name;
        this.symbol = symbol;
        this.price = price;
    }

    public Krypto(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public List<HistoricalData> getHistoricalData() {
        return historicalData;
    }

    public void setHistoricalData(List<HistoricalData> historicalDataSet) {
        this.historicalData = historicalDataSet;
    }

    public void addHistoricalData(HistoricalData historicalData) {
        this.historicalData.add(historicalData);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Instant getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Instant updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public List<Wallet> getWallets() {
        return wallets;
    }

    public void setWallets(List<Wallet> wallets) {
        this.wallets = wallets;
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
