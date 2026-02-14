package ru.zyryanova.LedgerService.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.NotificationCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zyryanova.LedgerService.entity.LedgerEntry;
import ru.zyryanova.LedgerService.entity.LedgerOutbox;
import ru.zyryanova.LedgerService.repository.LedgerOutboxRepo;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class LedgerOutboxService {
    private final LedgerOutboxRepo ledgerOutboxRepo;
    private String topicName = "notification-created-topic";
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public LedgerOutboxService(LedgerOutboxRepo ledgerOutboxRepo, ObjectMapper objectMapper, KafkaTemplate<String, String> kafkaTemplate) {
        this.ledgerOutboxRepo = ledgerOutboxRepo;
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    public LedgerOutbox create(LedgerEntry ledgerEntry) throws JsonProcessingException {
        NotificationCreatedEvent notificationCreatedEvent = new NotificationCreatedEvent(ledgerEntry.getEventId(), "INFO", LocalDateTime.now());
        LedgerOutbox ledgerOutbox = new LedgerOutbox();
        ledgerOutbox.setTopic(topicName);
        ledgerOutbox.setEntryId(ledgerEntry.getEntryId());
        ledgerOutbox.setStatus("NEW");
        ledgerOutbox.setPayload(objectMapper.writeValueAsString(notificationCreatedEvent));
        ledgerOutbox.setEventId(UUID.randomUUID().toString());
        return ledgerOutbox;

    }
    @Transactional
    public void process(List<LedgerOutbox> list) throws ExecutionException, InterruptedException {
        System.out.println("START process");
        for(LedgerOutbox ledgerOutbox : list){
            try{
                kafkaTemplate.send(ledgerOutbox.getTopic(),String.valueOf(ledgerOutbox.getEntryId()), ledgerOutbox.getPayload()).get();
                ledgerOutboxRepo.delete(ledgerOutbox);
            }catch (Exception e){
                ledgerOutboxRepo.resetToNew(ledgerOutbox.getLedgerOutboxId());
            }
        }
        System.out.println("END OF process ");
    }

    @Transactional
    public List<LedgerOutbox> processBatch() {
        System.out.println("в шедулере process batch");
        List<LedgerOutbox> events = ledgerOutboxRepo.selectForProcessing(50);
        var now = LocalDateTime.now();
        for (LedgerOutbox e : events) {
            e.setStatus("PROCESSING");
            e.setLockedAt(now);
        }
        System.out.println("END OF process batch");
        return ledgerOutboxRepo.saveAll(events);

    }
}
