package ru.zyryanova.LedgerService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.zyryanova.LedgerService.entity.Account;
import ru.zyryanova.LedgerService.entity.LedgerOutbox;

import java.util.UUID;

@Repository
public interface AccountRepo extends JpaRepository<Account, Integer> {
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @NativeQuery(value = """
            with locked as(
                select owner_id, balance from accounts
                where owner_id in(:senderId, :recipientId)
                order by owner_id
                 for update
            ),
            ok as(
                select 1
                where (
                    (select count(*) from locked) = 2
                    and
                    (select balance from locked where owner_id =:senderId)>=:amount
                )
            )
            update accounts a
            set balance = case
                when a.owner_id =:senderId then a.balance -:amount
                when a.owner_id =:recipientId then a.balance +:amount
                else a.balance
            end
            where a.owner_id in (:senderId, :recipientId)
                and exists (select 1 from ok)
            """)
    int deltaAmount(@Param("senderId") String senderId, @Param("recipientId") String recipientId, @Param("amount") long amount);


}
