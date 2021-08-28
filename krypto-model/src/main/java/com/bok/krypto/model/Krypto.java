package com.bok.krypto.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Krypto {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Column
    private String symbol;

    @Column
    private Double networkFee;

    @Column
    private BigDecimal price;

    @Column
    @UpdateTimestamp
    private Instant updateTimestamp;

    @OneToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<HistoricalData> historicalData = new ArrayList<>();

    @OneToMany
    @ToString.Exclude
    private List<Wallet> wallets;


    public Krypto(String name, String symbol, BigDecimal price) {
        this.name = name;
        this.symbol = symbol;
        this.price = price;
    }

    public Krypto(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Krypto krypto = (Krypto) o;

        return new EqualsBuilder().append(name, krypto.name).append(symbol, krypto.symbol).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(name).append(symbol).toHashCode();
    }

    @Transactional
    public void addHistoricalData(HistoricalData historicalData) {
        this.historicalData.add(historicalData);
    }

    @Transactional
    public void addHistoricalData(Collection<HistoricalData> historicalData) {
        this.historicalData.addAll(historicalData);
    }
}
