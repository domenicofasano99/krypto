package com.bok.krypto.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Krypto {
    @Id
    private Long id;

    @Column
    private String name;

    @Column
    private Double networkFee;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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
