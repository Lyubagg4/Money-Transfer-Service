package ru.zyryanova.LedgerService.service;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zyryanova.LedgerService.entity.LedgerEntry;
import ru.zyryanova.LedgerService.repository.LedgerEntryRepo;
import ru.zyryanova.LedgerService.repository.LedgerOutboxRepo;

@Service
public class LedgerEntryService {
    private final LedgerEntryRepo ledgerEntryRepo;
    private final AccountService accountService;
    private final LedgerOutboxRepo ledgerOutboxRepo;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public LedgerEntryService(LedgerEntryRepo ledgerEntryRepo, AccountService accountService, LedgerOutboxRepo ledgerOutboxRepo) {
        this.ledgerEntryRepo = ledgerEntryRepo;
        this.accountService = accountService;
        this.ledgerOutboxRepo = ledgerOutboxRepo;
    }
    @Transactional
    public boolean createTransferEntry(LedgerEntry ledgerEntry){
        try{
            ledgerEntryRepo.saveAndFlush(ledgerEntry);
            accountService.deltaAmount(ledgerEntry.getSenderId(), ledgerEntry.getRecipientId(), ledgerEntry.getAmount());
            return true;
        }catch (DataIntegrityViolationException e){
            Throwable root = NestedExceptionUtils.getRootCause(e);
            if (root instanceof ConstraintViolationException cve) {
                String sqlState = cve.getSQLException() != null ? cve.getSQLException().getSQLState() : null;
                if ("23505".equals(sqlState)) {
                    logger.warn("Duplicate eventId={}, ignoring", ledgerEntry.getEventId());
                    return false;
                }
            }
            throw e;
        }
    }



}
