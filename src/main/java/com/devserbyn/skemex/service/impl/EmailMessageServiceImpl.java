package com.devserbyn.skemex.service.impl;

import com.devserbyn.skemex.service.EmailMessageService;
import com.devserbyn.skemex.controller.dto.EmailMessage;
import com.devserbyn.skemex.entity.Employee;
import com.devserbyn.skemex.entity.Reservation;

import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import static com.devserbyn.skemex.constants.MSG_CONSTANTS.*;

@Service
public class EmailMessageServiceImpl implements EmailMessageService {

    @Override
    public EmailMessage expiringApproverMsg(String approverMail, String approverName,
                                            List<Reservation> reservationList) {
        StringBuilder expiringReportText = new StringBuilder();
        expiringReportText.append(String.format(WELCOME_WORDS, approverName));
        reservationList.forEach(r -> expiringReportText.append(expiringApproverMsgBuild(
                                                                r.getRequester().getNickname(),
                                                                r.getWorkspace().getRoom().getTitle(),
                                                                r.getWorkspace().getNumber()
        )));
        return EmailMessage.builder().target(approverMail)
                                     .title(EMAIL_RESERV_EXPIRED_APPROVER_TITLE)
                                     .content(expiringReportText.toString()).build();
    }

    @Override
    public EmailMessage expiringRequesterMsg(Reservation reservation) {
        return basicReservationMsg(reservation, EMAIL_RESERV_EXPIRED_REQUESTER_TITLE,
                                                EMAIL_RESERV_EXPIRED_REQUESTER);
    }

    @Override
    public EmailMessage reservationOpenMsg(Employee admin, Reservation reservation) {
        String content = reservationOpenMsgBuild(admin.getFirstName(),
                                                 reservation.getWorkspace().getRoom().getTitle(),
                                                 reservation.getWorkspace().getNumber(),
                                                 reservation.getRequester().getNickname(),
                                                 reservation.getStartTime(),
                                                 reservation.getEndTime());
        return EmailMessage.builder().target(admin.getEmail())
                                     .title(EMAIL_RESERV_OPEN_TITLE)
                                     .content(content).build();
    }

    @Override
    public EmailMessage reservationApprovedMsg(Reservation reservation) {
        String content = reservationApprovedMsgBuild(reservation.getWorkspace().getRoom().getTitle(),
                                                     reservation.getWorkspace().getNumber(),
                                                     reservation.getStartTime(),
                                                     reservation.getEndTime());
        return EmailMessage.builder().target(reservation.getRequester().getEmail())
                                     .title(EMAIL_RESERV_APPR_REQUESTER_TITLE)
                                     .content(content).build();
    }

    @Override
    public EmailMessage applicationWorkReportMsg(String targetEmail, String jvmStartTimeStr,
                                                 int relErrorLogsSize, int archErrorLogsSize,
                                                 String memUsageStr, String clientHostErrorsPage,
                                                 File attachment) {
        String content = applicationWorkReportMsgBuild(jvmStartTimeStr, relErrorLogsSize,
                                                       archErrorLogsSize, memUsageStr,
                                                       clientHostErrorsPage);
        return EmailMessage.builder().target(targetEmail)
                                     .title(EMAIL_ERROR_REPORT_TITLE)
                                     .content(content)
                                     .attachment(attachment).build();
    }

    @Override
    public EmailMessage reservationDeclinedMsg(Reservation reservation) {
        return basicReservationMsg(reservation, EMAIL_RESERV_DECLINED_TITLE, EMAIL_RESERV_DECLINED);
    }

    private EmailMessage basicReservationMsg(Reservation reservation, String title, String template) {
        String requesterName = reservation.getRequester().getFirstName();
        String roomTitle = reservation.getWorkspace().getRoom().getTitle();
        Integer workspaceNumber = reservation.getWorkspace().getNumber();
        String content = basicReservationMsgBuild(template, requesterName, roomTitle,
                                                workspaceNumber);
        return EmailMessage.builder().target(reservation.getRequester().getEmail())
                                     .title(title)
                                     .content(content).build();
    }

    private String expiringApproverMsgBuild(String nickname, String title, Integer number) {
        return String.format(EMAIL_RESERV_EXPIRED_APPROVER, nickname, title, number);
    }

    private String basicReservationMsgBuild(String template, String requesterName,
                                            String roomTitle, Integer workspaceNumber) {
        return String.format(template, requesterName, roomTitle, workspaceNumber);
    }

    private String reservationOpenMsgBuild(String adminName, String roomTitle,
                                           Integer workspaceNumber, String requesterNickname,
                                           LocalDate startTime, LocalDate endTime) {
        return String.format(EMAIL_RESERV_OPEN, adminName, roomTitle, workspaceNumber,
                                                requesterNickname, startTime, endTime);
    }

    private String reservationApprovedMsgBuild(String roomTitle, Integer workspaceNumber,
                                               LocalDate startTime, LocalDate endTime) {
        return String.format(EMAIL_RESERV_APPR, roomTitle, workspaceNumber, startTime, endTime);
    }

    private String applicationWorkReportMsgBuild(String jvmStartTimeStr, int relErrorLogsSize,
                                                 int archErrorLogsSize, String memUsageStr,
                                                 String clientHostErrorsPage) {
        return String.format(EMAIL_ERROR_REPORT_MSG, jvmStartTimeStr, relErrorLogsSize,
                                                     archErrorLogsSize, memUsageStr, clientHostErrorsPage);
    }
}
