package com.bok.krypto.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
@ToString
@Entity
@DiscriminatorValue("transaction")
public class Transaction extends Activity {

    @Column
    @Enumerated(EnumType.STRING)
    public Type type;

    @ManyToOne
    private Wallet wallet;


    public Transaction(Type type) {
        super();
        this.type = type;
    }

    public enum Type {
        PURCHASE,
        SELL
    }
}
