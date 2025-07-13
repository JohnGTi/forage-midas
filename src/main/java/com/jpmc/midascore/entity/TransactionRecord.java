package com.jpmc.midascore.entity;

import jakarta.persistence.*;

@Entity
public class TransactionRecord {

    /**
     * Task 3 assumes no duplicate transactions; though Spring Data JPA necessitates a primary key.
     */
    @Id
    @GeneratedValue()
    private long primaryKey;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="SENDER_ID", nullable = false)
    private UserRecord sender;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="RECIPIENT_ID", nullable = false)
    private UserRecord recipient;

    @Column(nullable = false)
    private float amount;

    @Column(nullable = false)
    private float incentive;

    protected TransactionRecord() {
    }

    public TransactionRecord(UserRecord sender, UserRecord recipient, float amount, float incentive) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.incentive = incentive;
    }

    public TransactionRecord(UserRecord sender, UserRecord recipient, float amount) {
        this(sender, recipient, amount, 0f);
    }
}
