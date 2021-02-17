package com.bok.krypto.model;

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
}
