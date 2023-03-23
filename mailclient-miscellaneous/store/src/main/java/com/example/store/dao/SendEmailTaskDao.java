package com.example.store.dao;

import com.example.store.entities.SendEmailTaskEntity;
import com.example.store.repositories.SendEmailTaskRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class SendEmailTaskDao {

    SendEmailTaskRepository sendEmailTaskRepository;

    @Transactional
    public SendEmailTaskEntity save(SendEmailTaskEntity entity) {
        return sendEmailTaskRepository.save(entity);
    }

    public List<SendEmailTaskEntity> findAllNotProcessed() {
        return sendEmailTaskRepository.findAllNotProcessed();
    }

    @Transactional
    public void markAsProcessed(SendEmailTaskEntity sendEmailTask) {
        sendEmailTaskRepository.markAsProcessed(sendEmailTask.getId());
    }

    public void updateLatestTryAt(SendEmailTaskEntity sendEmailTaskEntity) {
        sendEmailTaskRepository.updateLatestTryAt(sendEmailTaskEntity.getId());
    }
}
