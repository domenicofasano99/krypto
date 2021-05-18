package com.bok.krypto.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
import javax.persistence.PrePersist;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@DiscriminatorColumn(name = "transaction_type",
        discriminatorType = DiscriminatorType.STRING)
public abstract class Activity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 36, unique = true, updatable = false)
    private String publicId;

    @Column(updatable = false)
    @CreationTimestamp
    private Instant creationTimestamp;

    @Column
    @UpdateTimestamp
    private Instant updateTimestamp;

    @ManyToOne
    private Account account;

    @Column(updatable = false)
    private BigDecimal amount;

    @Column
    private Double fee;

    @Column
    @Enumerated(EnumType.STRING)
    public Status status;

    public enum Status {
        AUTHORIZED,
        PENDING,
        SETTLED,
        REJECTED
    }

    @PrePersist
    public void prePersist() {
        this.status = Status.PENDING;
        this.publicId = UUID.randomUUID().toString();
    }
}
