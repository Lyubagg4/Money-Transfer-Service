package org.example;

import java.time.LocalDateTime;

public class NotificationCreatedEvent {
    private String eventId;// текст лога
    private String level;  // INFO, ERROR, WARN
    private LocalDateTime createdAt;

    public NotificationCreatedEvent(String eventId, String level, LocalDateTime createdAt) {
        this.eventId = eventId;
        this.level = level;
        this.createdAt = createdAt;
    }

    public NotificationCreatedEvent() {
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
}
