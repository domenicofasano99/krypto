package com.bok.krypto.audit.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class AuditLog {

    @Id
    @GeneratedValue
    private Long id;


    public AuditLog() {
        //hibernate
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
