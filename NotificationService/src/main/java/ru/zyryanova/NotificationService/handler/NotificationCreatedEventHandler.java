package ru.zyryanova.NotificationService.handler;

import org.example.NotificationCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.zyryanova.NotificationService.entity.Notification;
import ru.zyryanova.NotificationService.error.NonRetryableException;
import ru.zyryanova.NotificationService.service.NotificationService;

@Component
public class NotificationCreatedEventHandler{
    private final NotificationService notificationService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public NotificationCreatedEventHandler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "notification-created-topic",
            groupId = "notification-created-events",
            containerFactory = "kafkaListenerContainerFactory")
    public void handle(@Payload NotificationCreatedEvent notificationCreatedEvent){
        logger.info("received notification from ledger service");
        boolean created = notificationService.create(notificationCreatedEvent);
        if (created) {
            logger.info("notification saved, eventId={}", notificationCreatedEvent.getEventId());
        } else {
            logger.info("duplicate ignored, eventId={}", notificationCreatedEvent.getEventId());
        }


    }


}
