package com.example.worker.scheduled;

import com.example.store.dao.SendEmailTaskDao;
import com.example.store.entities.SendEmailTaskEntity;
import com.example.worker.service.EmailClientApi;
import com.example.worker.service.RedisLock;
import com.example.worker.service.RedisLockWrapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class SendEmailTaskScheduler {

    SendEmailTaskDao sendEmailTaskDao;

    RedisLock redisLock;

    EmailClientApi emailClientApi;

    RedisLockWrapper redisLockWrapper;

    private static final String SEND_EMAIL_TASK_KEY_FORMAT = "mailclient:send:email:tasl:%s";

    @Scheduled(cron = "0/5 * * * * *")
    public void executeSendEmailTask() {

        log.info("Worker start execution.");

        List<Long> sendEmailTaskEntityIdsList = sendEmailTaskDao.findNotProcessedIds();

        for(Long sendEmailTaskId : sendEmailTaskEntityIdsList) {
            String sendEmailTaskKey = getSendEmailTaskKey(sendEmailTaskId);

            redisLockWrapper.lockAndExecuteTask(
                    sendEmailTaskKey,
                    Duration.ofSeconds(5),
                    () -> sendEmail(sendEmailTaskId)
            );
        }
    }

    private void sendEmail(Long sendEmailTaskId) {
        Optional<SendEmailTaskEntity> optionalSendEmailTask = sendEmailTaskDao
                .findNotProcessedById(sendEmailTaskId);

        if(optionalSendEmailTask.isEmpty()) {
            log.info("Task %d already processed.".formatted(sendEmailTaskId));
            return;
        }

        SendEmailTaskEntity sendEmailTask = optionalSendEmailTask.get();

        String destinationEmail = sendEmailTask.getDestinationEmail();
        String message = sendEmailTask.getMessage();

        boolean delivered = emailClientApi.sendEmail(destinationEmail, message);

        if (delivered) {
            log.debug("Task %d processed.".formatted(sendEmailTask.getId()));
            sendEmailTaskDao.markAsProcessed(sendEmailTask);
            return;
        }

        log.warn("Task %d returned to process .".formatted(sendEmailTask.getId()));
        sendEmailTaskDao.updateLatestTryAt(sendEmailTask);
    }

    private static String getSendEmailTaskKey(Long taskId) {
        return SEND_EMAIL_TASK_KEY_FORMAT.formatted(taskId);
    }
}
