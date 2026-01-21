package ru.zyryanova.TransferService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.zyryanova.TransferService.entity.TransferOutbox;

import java.util.List;
import java.util.Map;

@Repository
public interface TransferOutboxRepo extends JpaRepository<TransferOutbox, Integer> {
    @Query(value = """
        SELECT *
        FROM transfer_outbox
        WHERE status = 'NEW'
        ORDER BY created_at
        FOR UPDATE SKIP LOCKED
        LIMIT :limit
        """, nativeQuery = true)
    List<TransferOutbox> selectForProcessing(@Param("limit") int limit);
}
