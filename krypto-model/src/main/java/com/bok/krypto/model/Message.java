package com.bok.krypto.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Message {

    @Column(updatable = false)
    @Enumerated(EnumType.STRING)
    public Status status;
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private Long accountId;
    @Column
    private Long activityId;
    @Column
    private Long activityPublicId;

    public Message() {
        //hibernate
    }

    public Message(Long accountId, Long activityId, Long activityPublicId) {
        this.accountId = accountId;
        this.activityId = activityId;
        this.activityPublicId = activityPublicId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getActivityPublicId() {
        return activityPublicId;
    }

    public void setActivityPublicId(Long activityPublicId) {
        this.activityPublicId = activityPublicId;
    }

    public enum Status {
        ENQUEUED,
        DEQUEUED,
        REJECTED
    }
}
