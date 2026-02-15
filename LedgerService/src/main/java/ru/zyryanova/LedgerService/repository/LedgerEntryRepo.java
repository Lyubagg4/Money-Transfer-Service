package ru.zyryanova.LedgerService.repository;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.zyryanova.LedgerService.entity.LedgerEntry;
import ru.zyryanova.LedgerService.entity.LedgerOutbox;

import java.time.LocalDateTime;

@Repository
public interface
LedgerEntryRepo extends JpaRepository<LedgerEntry, Integer> {
    @Modifying
    @Query(value = """
            insert into ledger_entry (sender_id, recipient_id, amount, transfer_id, event_id, created_at)
            values (:senderId, :recipientId, :amount, :transferId, :eventId, :createdAt)
            """, nativeQuery = true)
    int insertIgnore(@Param("senderId") String senderId, @Param("recipientId") String recipientId,
                     @Param("amount") long amount, @Param("transferId") int transferId,
                     @Param("eventId") String eventId, @Param("createdAt") LocalDateTime createdAt);
}


