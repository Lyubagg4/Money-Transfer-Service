package ru.zyryanova.TransferService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.zyryanova.TransferService.entity.TransferOutbox;

import java.util.List;
import java.util.concurrent.ExecutionException;


@Service
public class TransferOutboxScheduler {
    private final TransferOutboxService transferOutboxService;


    @Autowired
    public TransferOutboxScheduler(TransferOutboxService transferOutboxService) {
        this.transferOutboxService = transferOutboxService;
    }

    @Scheduled(fixedDelay = 10_000)
    public void run() throws ExecutionException, InterruptedException {
        List<TransferOutbox> list = transferOutboxService.processBatch();
        transferOutboxService.process(list);
    }

}
