package org.example;

import java.time.LocalDateTime;

public class NotificationCreatedEvent {
    private String message;    // текст лога
    private String level;  // INFO, ERROR, WARN
    private LocalDateTime timestamp;

    public NotificationCreatedEvent() {
    }

    public NotificationCreatedEvent(String message, String level, LocalDateTime timestamp) {
        this.message = message;
        this.level = level;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
