package com.example.store.repositories;

import com.example.store.entities.SendEmailTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SendEmailTaskRepository extends JpaRepository<SendEmailTaskEntity, Long> {

    @Query("""
        select task
        from SendEmailTaskEntity task
        where task.processedAt is null
        order by task.createdAt
    """)
    List<SendEmailTaskEntity> findAllNotProcessed();

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
