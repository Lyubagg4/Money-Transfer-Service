package ru.zyryanova.TransferService.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.TransferCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<String> create(Transfer transfer){
        String eventId = UUID.randomUUID().toString();

        TransferCreatedEvent transferCreatedEvent = new TransferCreatedEvent();
        transferCreatedEvent.setEventId(eventId);
        createEvent(transfer,transferCreatedEvent);

        TransferOutbox transferOutbox = new TransferOutbox();
        transferOutbox.setTransferId(transfer.getTransferId());
        transferOutbox.setEventId(eventId);
        transferOutbox.setCreatedAt(LocalDateTime.now());
        try {
            transferOutbox.setPayload(objectMapper.writeValueAsString(transferCreatedEvent));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        transferOutbox.setTopic(topicName);
        transferOutbox.setStatus("NEW");
        transferOutboxRepo.save(transferOutbox);


        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Transactional
    public void processBatch() throws ExecutionException, InterruptedException {
        List<TransferOutbox> events = transferOutboxRepo.selectForProcessing(50);
        for(TransferOutbox e: events){
            process(e);
        }
    }

    public void process(TransferOutbox transferOutbox) throws ExecutionException, InterruptedException {
        kafkaTemplate.send(transferOutbox.getTopic(),String.valueOf(transferOutbox.getTransferId()), transferOutbox.getPayload()).get();
        transferOutboxRepo.delete(transferOutbox);

    }
    public TransferCreatedEvent createEvent(Transfer transfer, TransferCreatedEvent transferCreatedEvent){
        transferCreatedEvent.setId(transfer.getTransferId());
        transferCreatedEvent.setRecipientId(transfer.getRecipientId());
        transferCreatedEvent.setAmount(transfer.getAmount());
        transferCreatedEvent.setSenderId(transfer.getSenderId());
        transferCreatedEvent.setCreatedAt(transfer.getCreatedAt());
        return transferCreatedEvent;

    }
}
