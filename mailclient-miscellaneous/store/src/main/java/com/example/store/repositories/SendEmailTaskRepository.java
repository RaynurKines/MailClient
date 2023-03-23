package com.example.store.repositories;

import com.example.store.entities.SendEmailTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface SendEmailTaskRepository extends JpaRepository<SendEmailTaskEntity, Long> {

    @Query("""
        select task.id
        from SendEmailTaskEntity task
        where task.processedAt is null
            and (task.latestTryAt is null or task.latestTryAt <= :latestTryAtLte)
        order by task.createdAt
    """)
    List<Long> findNotProcessedIds(Instant latestTryAtLte);

    @Query("""
        select task
        from SendEmailTaskEntity task
        where task.processedAt is null
            and task.id = :sendEmailTaskId
            and (task.latestTryAt is null or task.latestTryAt <= :latestTryAtLte)
        order by task.createdAt
    """)
    Optional<SendEmailTaskEntity> findNotProcessedById(Long sendEmailTaskId, Instant latestTryAtLte);

    @Modifying
    @Query("""
        update SendEmailTaskEntity task
        set task.processedAt = now()
        where task.id = :id
    """)
    void markAsProcessed(Long id);

    @Modifying
    @Query("""
        update SendEmailTaskEntity task
        set task.latestTryAt = now()
        where task.id = :id
    """)
    void updateLatestTryAt(Long id);
}
