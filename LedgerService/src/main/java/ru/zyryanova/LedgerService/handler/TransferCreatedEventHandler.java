package ru.zyryanova.LedgerService.handler;

import org.example.NotificationCreatedEvent;
import org.example.TransferCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.zyryanova.LedgerService.entity.LedgerEntry;
import ru.zyryanova.LedgerService.service.AccountService;
import ru.zyryanova.LedgerService.service.LedgerEntryService;

import java.time.LocalDateTime;

@Component
public class TransferCreatedEventHandler {
    private final AccountService accountService;
    private final LedgerEntryService ledgerEntryService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private String topicName = "notification-created-topic";
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public TransferCreatedEventHandler(AccountService accountService, LedgerEntryService ledgerEntryService, KafkaTemplate<String, Object> kafkaTemplate) {
        this.accountService = accountService;
        this.ledgerEntryService = ledgerEntryService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "transfer-created-topic",
            groupId = "transfer-created-events",
            containerFactory = "kafkaListenerContainerFactory")
    public void handle(@Payload TransferCreatedEvent transferCreatedEvent){
        logger.info("!!!!"+transferCreatedEvent.getEventId()+"!!!!");
        LedgerEntry ledgerEntry = new LedgerEntry();
        ledgerEntry.setEventId(transferCreatedEvent.getEventId());
        ledgerEntry.setSenderId(transferCreatedEvent.getSenderId());
        ledgerEntry.setRecipientId(transferCreatedEvent.getRecipientId());
        ledgerEntry.setAmount(transferCreatedEvent.getAmount());
        ledgerEntry.setCreatedAt(transferCreatedEvent.getCreatedAt());
        ledgerEntry.setTransferId(transferCreatedEvent.getTransfer_id());
        boolean result = ledgerEntryService.createTransferEntry(ledgerEntry);
        if(result){
            NotificationCreatedEvent notificationCreatedEvent = new NotificationCreatedEvent(ledgerEntry.getEventId(), "INFO", LocalDateTime.now());
            kafkaTemplate.send(topicName,notificationCreatedEvent);
        }

    }
}
