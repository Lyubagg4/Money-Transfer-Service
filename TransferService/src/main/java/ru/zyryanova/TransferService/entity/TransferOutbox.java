package ru.zyryanova.TransferService.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.LocalDateTime;

@Entity
@Table(name = "transfer_outbox")
public class TransferOutbox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="outbox_id")
    private int outboxId;

    @Column(name="event_id", unique = true, nullable = false)
    private String eventId;

    @Column(name="transfer_id")
    private int transferId;

    @Column(name="topic")
    private String topic;

    @Column(name="payload")
    private String payload;

    @Column(name="created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name="locked_at")
    private LocalDateTime lockedAt;

    @Column(name="status")
    private String status;


    public TransferOutbox() {
    }

    public int getOutboxId() {
        return outboxId;
    }

    public void setOutboxId(int outboxId) {
        this.outboxId = outboxId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLocked_at() {
        return lockedAt;
    }

    public void setLocked_at(LocalDateTime locked_at) {
        this.lockedAt = locked_at;
    }
}
