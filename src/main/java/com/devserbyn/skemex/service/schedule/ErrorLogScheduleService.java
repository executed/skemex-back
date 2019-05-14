package com.devserbyn.skemex.service.schedule;

import com.devserbyn.skemex.service.EmailMessageService;
import com.devserbyn.skemex.service.EmailService;
import com.devserbyn.skemex.service.ErrorLogService;
import com.devserbyn.skemex.controller.dto.EmailMessage;
import com.devserbyn.skemex.entity.ErrorLog;
import com.devserbyn.skemex.utility.ApplicationValuesUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@Slf4j
@PropertySource ("classpath:mail.properties")
@PropertySource ("classpath:dataSource.properties")
@PropertySource ("classpath:security.properties")
public class ErrorLogScheduleService {

    @Value("${errorLog.daysOfRelevance}")
    private int daysOfRelevance;
    @Value ("${email.report}")
    private String receiverEmail;
    @Value ("#{corsURl}")
    private String clientHost;

    private final ErrorLogService service;
    private final EmailService emailService;
    private final EmailMessageService emailMsgService;

    @Autowired
    public ErrorLogScheduleService(ErrorLogService service, EmailService emailService,
                                   EmailMessageService emailMsgService) {
        this.service = service;
        this.emailService = emailService;
        this.emailMsgService = emailMsgService;
    }

    /** Method, which is scheduled via Cron and contains class-specific method call scenario */
    @Scheduled (cron = CronSchedule.EVERY_MONDAY_MIDNIGHT)
    public void init() throws Exception {
        log.debug("Starting Cron task: {}", this.getClass().getName());
        archiveOldLogs();
        sendApplicationWorkReport();
    }

    /** Method, which deletes error logs with creation time before specified */
    private void archiveOldLogs() {
        log.trace("Archiving error logs with creation time before {} days", daysOfRelevance);
        LocalDateTime oldLogsEndTime = LocalDateTime.now().minusDays(daysOfRelevance);
        Timestamp oldLogsEndTimeStamp = Timestamp.valueOf(oldLogsEndTime);
        service.archiveOldLogs(oldLogsEndTimeStamp);
    }

    /** Method, which sends email with error report to specified receiver email*/
    public void sendApplicationWorkReport() {
        List<ErrorLog> relevantErrorLogs = service.findAll();
        int relevantErrorLogsSize = relevantErrorLogs.size();
        int archivedErrorLogsSize = service.findErrorsSize(true).intValue();

        log.trace("Sending application work report to {}", receiverEmail);
        String clientHostErrorsPage = String.format("%s/errors", clientHost);
        String JVMStartTimeString = ApplicationValuesUtility.getJVMStartTime().toString();
        String memUsageString = ApplicationValuesUtility.getHeapMemUsageString();
        File attachment = emailService.generateAttachmentWithData(relevantErrorLogs)
                                      .orElseThrow(IllegalArgumentException::new);
        EmailMessage msg = emailMsgService.applicationWorkReportMsg(receiverEmail, JVMStartTimeString,
                                                              relevantErrorLogsSize, archivedErrorLogsSize,
                                                              memUsageString, clientHostErrorsPage, attachment);
        emailService.sendEmail(msg);
    }
}
