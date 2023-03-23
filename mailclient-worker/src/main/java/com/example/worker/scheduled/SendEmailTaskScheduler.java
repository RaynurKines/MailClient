package com.example.worker.scheduled;

import com.example.store.dao.SendEmailTaskDao;
import com.example.store.repositories.SendEmailTaskRepository;
import com.example.worker.service.EmailClientApi;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class SendEmailTaskScheduler {

    SendEmailTaskDao sendEmailTaskDao;

    EmailClientApi emailClientApi;

    @Scheduled(cron = "*/5 * * * * *")
    public void executeSendEmailTask() {

        sendEmailTaskDao
                .findAllNotProcessed()
                .forEach(sendEmailTaskEntity -> {
                    String destinationEmail = sendEmailTaskEntity.getDestinationEmail();
                    String message = sendEmailTaskEntity.getMessage();
                    boolean delivered = emailClientApi.sendEmail(destinationEmail, message);

                    if(delivered) {
                        log.info("Task %d already processed.", sendEmailTaskEntity.getId());
                        sendEmailTaskDao.markAsProcessed(sendEmailTaskEntity);
                    }
                    else {
                        sendEmailTaskDao.updateLatestTryAt(sendEmailTaskEntity);
                    }
                });
    }
}
