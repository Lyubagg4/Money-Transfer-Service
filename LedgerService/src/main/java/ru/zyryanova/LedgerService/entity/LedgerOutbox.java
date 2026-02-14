package ru.zyryanova.LedgerService.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ledger_outbox")
public class LedgerOutbox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ledgerOutbox_id")
    private int ledgerOutboxId;

    @Column(name="event_id", unique = true, nullable = false)
    private String eventId;

    @Column(name="transfer_id")
    private int entryId;

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

    public LedgerOutbox() {
    }

    public int getLedgerOutboxId() {
        return ledgerOutboxId;
    }

    public void setLedgerOutboxId(int ledgerOutboxId) {
        this.ledgerOutboxId = ledgerOutboxId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }


    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLockedAt() {
        return lockedAt;
    }

    public void setLockedAt(LocalDateTime lockedAt) {
        this.lockedAt = lockedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
