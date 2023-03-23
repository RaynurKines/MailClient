package com.example.store.dao;

import com.example.store.entities.SendEmailTaskEntity;
import com.example.store.repositories.SendEmailTaskRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class SendEmailTaskDao {

    SendEmailTaskRepository sendEmailTaskRepository;

    private static final Duration TASK_EXECUTE_DURATION = Duration.ofSeconds(10);

    @Transactional
    public SendEmailTaskEntity save(SendEmailTaskEntity entity) {
        return sendEmailTaskRepository.save(entity);
    }

    public List<Long> findNotProcessedIds() {
        Instant latestTryAtLte = Instant.now().minus(TASK_EXECUTE_DURATION);
        return sendEmailTaskRepository.findNotProcessedIds(latestTryAtLte);
    }

    public Optional<SendEmailTaskEntity> findNotProcessedById(Long sendEmailTaskId) {
        Instant latestTryAtLte = Instant.now().minus(TASK_EXECUTE_DURATION);
        return sendEmailTaskRepository.findNotProcessedById(sendEmailTaskId, latestTryAtLte);
    }

    @Transactional
    public void markAsProcessed(SendEmailTaskEntity sendEmailTask) {
        sendEmailTaskRepository.markAsProcessed(sendEmailTask.getId());
    }

    @Transactional
    public void updateLatestTryAt(SendEmailTaskEntity sendEmailTaskEntity) {
        sendEmailTaskRepository.updateLatestTryAt(sendEmailTaskEntity.getId());
    }
}
