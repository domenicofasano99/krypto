package com.bok.krypto.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@MappedSuperclass
@ToString
@DiscriminatorColumn(name = "transaction_type",
        discriminatorType = DiscriminatorType.STRING)
public abstract class Activity {

    @Column
    @Enumerated(EnumType.STRING)
    public Status status;

    @Id
    @GeneratedValue
    private Long id;

    @Column
    @GeneratedValue
    private UUID publicId;

    @CreationTimestamp
    private Instant creationTimestamp;

    @UpdateTimestamp
    private Instant updateTimestamp;

    @ManyToOne(targetEntity = Account.class)
    private Account account;

    @Column
    private BigDecimal amount;

    @Column
    private Double fee;

    public Activity() {
        this.status = Status.PENDING;
        this.publicId = UUID.randomUUID();
    }

    public enum Status {
        AUTHORIZED, //if bank has approved
        PENDING, //at creation
        SETTLED,
        DECLINED
    }
}
