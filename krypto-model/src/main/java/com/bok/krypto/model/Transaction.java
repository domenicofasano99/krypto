package com.bok.krypto.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("transaction")
public class Transaction extends Activity {

    @ManyToOne
    private Wallet wallet;

    @Column
    @Enumerated(EnumType.STRING)
    public Type type;

    @Column
    private String bankAuthorizationId;


    public enum Type {
        PURCHASE,
        SELL
    }

    public Transaction(Type type) {
        super();
        this.type = type;
    }

    public Transaction(Type type, String bankAuthorizationId) {
        super();
        this.type = type;
        this.bankAuthorizationId = bankAuthorizationId;
    }
}
