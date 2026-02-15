package ru.zyryanova.NotificationService.reposotory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.zyryanova.NotificationService.entity.Notification;

import java.time.LocalDateTime;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, Integer> {
    @Modifying
    @Query(value = """   
          insert into notification (event_id, level, created_at, operation_time) 
          values (:eventId, :level, :createdAt, :operationTime)      
          on conflict (event_id) do nothing
    """, nativeQuery = true)
    int insertIgnore(@Param("eventId") String eventId, @Param("level") String level,
                     @Param("createdAt")LocalDateTime createdAt, @Param("operationTime") LocalDateTime operationTime);
}
