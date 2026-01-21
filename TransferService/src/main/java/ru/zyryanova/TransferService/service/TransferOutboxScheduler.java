package ru.zyryanova.TransferService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.zyryanova.TransferService.repository.TransferOutboxRepo;

import java.util.concurrent.ExecutionException;

@Service
public class TransferOutboxScheduler {
    private final TransferOutboxService transferOutboxService;


    @Autowired
    public TransferOutboxScheduler(TransferOutboxRepo transferOutboxRepo, TransferOutboxService transferOutboxService) {
        this.transferOutboxService = transferOutboxService;
    }

    @Scheduled(fixedDelay = 10_000)
    public void run() throws ExecutionException, InterruptedException {
        transferOutboxService.processBatch();
    }




}
