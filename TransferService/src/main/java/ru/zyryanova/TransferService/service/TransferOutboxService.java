package ru.zyryanova.TransferService.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.TransferCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zyryanova.TransferService.entity.Transfer;
import ru.zyryanova.TransferService.entity.TransferOutbox;
import ru.zyryanova.TransferService.repository.TransferOutboxRepo;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class TransferOutboxService {
    private final TransferOutboxRepo transferOutboxRepo;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topicName = "transfer-created-topic";


    @Autowired
    public TransferOutboxService(TransferOutboxRepo transferOutboxRepo, ObjectMapper objectMapper, KafkaTemplate kafkaTemplate) {
        this.transferOutboxRepo = transferOutboxRepo;
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void create(Transfer transfer) throws JsonProcessingException {
        String eventId = UUID.randomUUID().toString();

        TransferCreatedEvent transferCreatedEvent = new TransferCreatedEvent();
        transferCreatedEvent.setEventId(eventId);
        createEvent(transfer,transferCreatedEvent);

        TransferOutbox transferOutbox = new TransferOutbox();
        transferOutbox.setTransferId(transfer.getTransferId());
        transferOutbox.setEventId(eventId);
        transferOutbox.setPayload(objectMapper.writeValueAsString(transferCreatedEvent));
        transferOutbox.setTopic(topicName);
        transferOutbox.setStatus("NEW");
        transferOutboxRepo.save(transferOutbox);
    }

    @Transactional
    public List<TransferOutbox> processBatch() {
        List<TransferOutbox> events = transferOutboxRepo.selectForProcessing(50);
        var now = LocalDateTime.now();
        for (TransferOutbox e : events) {
            e.setStatus("PROCESSING");
            e.setLocked_at(now);
        }
        return transferOutboxRepo.saveAll(events);
    }
    @Transactional
    public void process(List<TransferOutbox> list) throws ExecutionException, InterruptedException {
        for(TransferOutbox transferOutbox: list){
            try{
                kafkaTemplate.send(transferOutbox.getTopic(),String.valueOf(transferOutbox.getTransferId()), transferOutbox.getPayload()).get();
                transferOutboxRepo.delete(transferOutbox);
            }catch (Exception e){
                transferOutboxRepo.resetToNew(transferOutbox.getOutboxId());
            }
        }
    }
    public TransferCreatedEvent createEvent(Transfer transfer, TransferCreatedEvent transferCreatedEvent){
        transferCreatedEvent.setTransfer_id(transfer.getTransferId());
        transferCreatedEvent.setRecipientId(transfer.getRecipientId());
        transferCreatedEvent.setAmount(transfer.getAmount());
        transferCreatedEvent.setSenderId(transfer.getSenderId());
        transferCreatedEvent.setCreatedAt(transfer.getCreatedAt());
        return transferCreatedEvent;

    }
}
