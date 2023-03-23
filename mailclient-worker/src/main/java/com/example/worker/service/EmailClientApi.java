package com.example.worker.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class EmailClientApi {

    JavaMailSender emailSender;

    public boolean sendEmail(String destinationEmail, String message) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(destinationEmail);
            simpleMailMessage.setSubject("This letter is auto sended by Raynur's mailClient. Don't worry");
            simpleMailMessage.setText(message);
            emailSender.send(simpleMailMessage);
        } catch (MailException mailException) {
            log.error("Mail was not sent");
            return false;
        }

        log.info("Email to %s successfully sent.".formatted(destinationEmail));
        return true;
    }
}
