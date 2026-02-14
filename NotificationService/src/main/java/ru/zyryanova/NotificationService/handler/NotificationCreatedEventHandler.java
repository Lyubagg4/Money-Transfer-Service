package ru.zyryanova.NotificationService.handler;

import org.example.NotificationCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class NotificationCreatedEventHandler{
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @KafkaListener(topics = "notification-created-topic",
            groupId = "notification-created-events",
            containerFactory = "kafkaListenerContainerFactory")
    public void handle(@Payload NotificationCreatedEvent notificationCreatedEvent){
        logger.info(notificationCreatedEvent.getMessage());
        System.out.println("дадада");
    }
}
