package ru.zyryanova.LedgerService.service;

import org.example.TransferCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zyryanova.LedgerService.entity.LedgerEntry;
import ru.zyryanova.LedgerService.repository.AccountRepo;
import ru.zyryanova.LedgerService.repository.LedgerEntryRepo;

@Service
public class LedgerEntryService {
    private final LedgerEntryRepo ledgerEntryRepo;
    private final AccountService accountService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public LedgerEntryService(LedgerEntryRepo ledgerEntryRepo, AccountService accountService) {
        this.ledgerEntryRepo = ledgerEntryRepo;
        this.accountService = accountService;
    }
    @Transactional
    public void createTransferEntry(LedgerEntry ledgerEntry){
        try {
            ledgerEntryRepo.save(ledgerEntry);
            accountService.deltaAmount(ledgerEntry.getSenderId(), ledgerEntry.getRecipientId(), ledgerEntry.getAmount());
        }catch (DataIntegrityViolationException e){
            logger.warn("дубликат");
        }

    }



}
