package com.devserbyn.skemex.service.impl;

import com.devserbyn.skemex.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.devserbyn.skemex.controller.dto.EmailMessage;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static com.devserbyn.skemex.constants.MSG_CONSTANTS.*;

@Component
@PropertySource ("classpath:mail.properties")
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Value ("email.email")
    private String from;

    @Value("classpath:mail/mail-template.html")
    private Resource mailTemplateResource;
    @Value("classpath:mail/temp-attachment.json")
    private Resource tempAttachmentResource;

    private final JavaMailSender mailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(@NotNull EmailMessage email) {
        if (email.getTarget() == null || email.getContent() == null) {
            return;
        }
        MimeMessage messageInstance = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(messageInstance, true);
            helper.setTo(email.getTarget());
            helper.setSubject(email.getTitle());
            String content = wrapInTemplate(email).orElseThrow(RuntimeException::new);
            helper.setText(content, true);
            if (email.getAttachment() != null) {
                helper.addAttachment(email.getTitle(), email.getAttachment());
            }

            mailSender.send(messageInstance);
        } catch (MessagingException e){
            log.debug("Something went wrong while sending email", e);
        }
        log.trace("Email sent - {}", email);
    }

    private Optional<String> wrapInTemplate(EmailMessage email) {
        String result = null;
        try {
            File templateFile = mailTemplateResource.getFile();
            result = FileUtils.readFileToString(templateFile, "UTF-8");
            result = result.replace(EMAIL_MAIN_TITLE_ID, email.getTitle())
                           .replace(EMAIL_CONTENT_ID, email.getContent());
        } catch (IOException e) {
            log.debug("Something wrong while wrapping email in template", e);
        }
        return Optional.ofNullable(result);
    }

    @Override
    public <T> Optional<File> generateAttachmentWithData(T data) {
        File file = null;
        try {
            file = tempAttachmentResource.getFile();
            new ObjectMapper().writeValue(file, data);
        } catch (IOException e) {
            log.debug("File with data for email wasn't generated properly", e);
        }
        return Optional.ofNullable(file);
    }
}
