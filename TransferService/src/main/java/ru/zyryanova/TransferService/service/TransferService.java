package ru.zyryanova.TransferService.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zyryanova.TransferService.entity.Transfer;
import ru.zyryanova.TransferService.entity.TransferDto;
import ru.zyryanova.TransferService.entity.TransferOutbox;
import ru.zyryanova.TransferService.mapper.TransferMapper;
import ru.zyryanova.TransferService.repository.TransferOutboxRepo;
import ru.zyryanova.TransferService.repository.TransferRepo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransferService {
    private final TransferRepo transferRepo;
    private final TransferMapper transferMapper;

    private final TransferOutboxService transferOutboxService;

    @Autowired
    public TransferService(TransferRepo transferRepo, TransferMapper transferMapper, TransferOutboxRepo transferOutboxRepo, TransferOutboxService transferOutboxService) {
        this.transferRepo = transferRepo;
        this.transferMapper = transferMapper;
        this.transferOutboxService = transferOutboxService;
    }

    @Transactional
    public void create(TransferDto transferDto) throws JsonProcessingException {
        Transfer transfer = convertToEntity(transferDto);
        Transfer savedTransfer = transferRepo.saveAndFlush(transfer);
        transferOutboxService.create(savedTransfer);
    }

    public Transfer convertToEntity(TransferDto transferDto){
        Transfer transfer = transferMapper.toEntity(transferDto);
        transfer.setStatus("CREATED");
        return transfer;
    }
}
