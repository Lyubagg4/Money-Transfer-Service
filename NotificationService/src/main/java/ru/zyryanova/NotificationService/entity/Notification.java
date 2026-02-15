package ru.zyryanova.NotificationService.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
public class Notification{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private int notificationId;

    @Column(name = "event_id", unique = true, nullable = false)
    private String eventId;

    @Column(name = "level")
    private String level;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "operation_time")
    private LocalDateTime operationTime;


    public Notification(int notificationId, String eventId, String level, LocalDateTime createdAt) {
        this.notificationId = notificationId;
        this.eventId = eventId;
        this.level = level;
        this.createdAt = createdAt;
    }

    public Notification() {
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getOperation_time() {
        return operationTime;
    }

    public void setOperation_time(LocalDateTime operation_time) {
        this.operationTime = operation_time;
    }
}
