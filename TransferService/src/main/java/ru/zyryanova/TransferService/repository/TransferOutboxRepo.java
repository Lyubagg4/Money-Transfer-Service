package ru.zyryanova.TransferService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.zyryanova.TransferService.entity.TransferOutbox;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransferOutboxRepo extends JpaRepository<TransferOutbox, Integer> {
    @Query(value = """
        select *
        from transfer_outbox
        where status = 'NEW'
        or (status='PROCESSING' and  locked_at < now() -  interval '5 minutes')
        order by created_at
        for update skip locked
        limit :limit
        """, nativeQuery = true)
    List<TransferOutbox> selectForProcessing(@Param("limit") int limit);

    @Modifying
    @Query(value = """
            update transfer_outbox
            set status = 'NEW', locked_at = null
            where outbox_id =:outboxId
            """, nativeQuery = true
    )
    void resetToNew(@Param("outboxId") int outboxId);
}
