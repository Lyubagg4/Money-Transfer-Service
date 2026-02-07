package ru.zyryanova.LedgerService.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import ru.zyryanova.LedgerService.entity.LedgerEntry;

@Repository
public interface
LedgerEntryRepo extends JpaRepository<LedgerEntry, Integer> {
}
