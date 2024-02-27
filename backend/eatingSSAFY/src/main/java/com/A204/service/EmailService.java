package com.A204.service;

import com.A204.dto.email.EmailMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSenderImpl sender;

    /**
     * email send
     */
    public void send(EmailMessage emailMessage) {
        MimeMessage message = sender.createMimeMessage();

        try {
            // multipart 모드로 helper 생성
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // content 설정
            helper.setSubject(emailMessage.subject());
            helper.setTo(emailMessage.to());
            helper.setText(emailMessage.text());

            // attachment 설정
            if (!emailMessage.attachFileName().isEmpty() && !emailMessage.attachFilePath().isEmpty()) {
                FileSystemResource file = new FileSystemResource(new File(emailMessage.attachFilePath()));
                helper.addAttachment(emailMessage.attachFileName(), file);
            }
            sender.send(message);
        } catch (MessagingException e) {
            logger.error(e.getMessage());
        }
    }
}
