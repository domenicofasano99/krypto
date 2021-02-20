package com.bok.krypto.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Krypto {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Column(unique = true)
    private String code;

    @Column
    private Double networkFee;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
