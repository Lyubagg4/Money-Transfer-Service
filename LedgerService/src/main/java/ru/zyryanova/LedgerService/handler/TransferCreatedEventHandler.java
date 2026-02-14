package ru.zyryanova.LedgerService.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import ru.zyryanova.LedgerService.entity.LedgerOutbox;
import ru.zyryanova.LedgerService.repository.LedgerOutboxRepo;
import ru.zyryanova.LedgerService.service.AccountService;
import ru.zyryanova.LedgerService.service.LedgerEntryService;
import ru.zyryanova.LedgerService.service.LedgerOutboxService;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class TransferCreatedEventHandler {
    private final AccountService accountService;
    private final LedgerEntryService ledgerEntryService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final LedgerOutboxService ledgerOutboxService;
    private final LedgerOutboxRepo ledgerOutboxRepo;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public TransferCreatedEventHandler(AccountService accountService, LedgerEntryService ledgerEntryService, KafkaTemplate<String, String> kafkaTemplate, LedgerOutboxService ledgerOutboxService, LedgerOutboxRepo ledgerOutboxRepo) {
        this.accountService = accountService;
        this.ledgerEntryService = ledgerEntryService;
        this.kafkaTemplate = kafkaTemplate;
        this.ledgerOutboxService = ledgerOutboxService;
        this.ledgerOutboxRepo = ledgerOutboxRepo;
    }

    @KafkaListener(topics = "transfer-created-topic",
            groupId = "transfer-created-events",
            containerFactory = "kafkaListenerContainerFactory")
    public void handle(@Payload TransferCreatedEvent transferCreatedEvent) throws JsonProcessingException {
        System.out.println("ЗАШЛИ В ХЭНДЛЕР");
        LedgerEntry ledgerEntry = new LedgerEntry();
        ledgerEntry.setEventId(transferCreatedEvent.getEventId());
        ledgerEntry.setSenderId(transferCreatedEvent.getSenderId());
        ledgerEntry.setRecipientId(transferCreatedEvent.getRecipientId());
        ledgerEntry.setAmount(transferCreatedEvent.getAmount());
        ledgerEntry.setCreatedAt(transferCreatedEvent.getCreatedAt());
        ledgerEntry.setTransferId(transferCreatedEvent.getTransfer_id());
        boolean result = ledgerEntryService.createTransferEntry(ledgerEntry);
        System.out.println("перед result");
        if(result){
            ledgerOutboxRepo.save(ledgerOutboxService.create(ledgerEntry));
        }
        System.out.println("ВЫШЛИ ИЗ ХЭНДЛЕР");

    }

}
