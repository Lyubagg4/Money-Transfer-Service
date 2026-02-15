package ru.zyryanova.NotificationService.service;

import org.example.NotificationCreatedEvent;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zyryanova.NotificationService.entity.Notification;
import ru.zyryanova.NotificationService.error.NonRetryableException;
import ru.zyryanova.NotificationService.error.RetryableException;
import ru.zyryanova.NotificationService.reposotory.NotificationRepo;

import java.time.LocalDateTime;

@Service
public class NotificationService {
    private final NotificationRepo notificationRepo;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public NotificationService(NotificationRepo notificationRepo) {
        this.notificationRepo = notificationRepo;
    }

    @Transactional
    public boolean create(NotificationCreatedEvent notificationCreatedEvent){
        Notification notification = new Notification();
        notification.setLevel(notificationCreatedEvent.getLevel());
        notification.setCreatedAt(LocalDateTime.now());
        notification.setEventId(notificationCreatedEvent.getEventId());
        notification.setOperation_time(notificationCreatedEvent.getCreatedAt());
        int insented = notificationRepo.insertIgnore(
                notification.getEventId(),
                notification.getLevel(),
                notification.getCreatedAt(),
                notification.getOperation_time());
        return insented==1;

    }
}
