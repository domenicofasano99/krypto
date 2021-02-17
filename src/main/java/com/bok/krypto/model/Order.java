package com.bok.krypto.model;

import javax.persistence.*;

@Entity
public class Order {
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
}
