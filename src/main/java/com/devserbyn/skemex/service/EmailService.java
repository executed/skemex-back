package com.devserbyn.skemex.service;

import com.devserbyn.skemex.controller.dto.EmailMessage;

import java.io.File;
import java.util.Optional;

public interface EmailService {

    void sendEmail(EmailMessage email);

    <T> Optional<File> generateAttachmentWithData(T data);
}
