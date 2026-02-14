package ru.zyryanova.LedgerService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.zyryanova.LedgerService.entity.LedgerOutbox;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class LedgerScheduler{
    private final LedgerOutboxService ledgerOutboxService;

    @Autowired
    public LedgerScheduler(LedgerOutboxService ledgerOutboxService) {
        this.ledgerOutboxService = ledgerOutboxService;
    }

    @Scheduled(fixedDelay = 10_000)
    public void run() throws ExecutionException, InterruptedException {
        List<LedgerOutbox> list = ledgerOutboxService.processBatch();
        ledgerOutboxService.process(list);
    }

}
