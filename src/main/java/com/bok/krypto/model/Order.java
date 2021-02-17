package com.bok.krypto.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Order implements Serializable {
    @Id
    private Long id;

    @ManyToOne
    private Krypto krypto;

    @Column
    private Double kryptoAmount;

    @Column
    @Enumerated(EnumType.STRING)
    private Type orderType;

    public Order() {
        /* hibernate */
    }

    public Order(Krypto krypto, Double kryptoAmount) {
        this.krypto = krypto;
        this.kryptoAmount = kryptoAmount;
    }

    public enum Type {
        BUY,
        SELL
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Krypto getKrypto() {
        return krypto;
    }

    public void setKrypto(Krypto krypto) {
        this.krypto = krypto;
    }

    public Double getKryptoAmount() {
        return kryptoAmount;
    }

    public void setKryptoAmount(Double kryptoAmount) {
        this.kryptoAmount = kryptoAmount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("krypto", krypto)
                .append("kryptoAmount", kryptoAmount)
                .append("orderType", orderType)
                .toString();
    }
}
