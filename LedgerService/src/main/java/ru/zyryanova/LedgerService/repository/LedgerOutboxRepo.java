package ru.zyryanova.LedgerService.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.zyryanova.LedgerService.entity.LedgerOutbox;

import java.util.List;

public interface LedgerOutboxRepo extends JpaRepository<LedgerOutbox, Integer> {
    @Query(value = """
        select *
        from ledger_outbox
        where status = 'NEW'
        or (status='PROCESSING' and  locked_at < now() -  interval '5 minutes')
        order by created_at
        for update skip locked
        limit :limit
        """, nativeQuery = true)
    List<LedgerOutbox> selectForProcessing(@Param("limit") int limit);

    @Modifying
    @Query(value = """
            update ledger_outbox
            set status = 'NEW', locked_at = null
            where outbox_id =:outboxId
            """, nativeQuery = true
    )
    void resetToNew(@Param("outboxId") int outboxId);

}
