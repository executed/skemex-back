package com.devserbyn.skemex.service;

import com.devserbyn.skemex.controller.dto.EmailMessage;
import com.devserbyn.skemex.entity.Employee;
import com.devserbyn.skemex.entity.Reservation;

import java.io.File;
import java.util.List;

public interface EmailMessageService {

    EmailMessage expiringApproverMsg(String approverMail, String approverName,
                                     List<Reservation> reservationList);

    EmailMessage reservationDeclinedMsg(Reservation reservation);

    EmailMessage expiringRequesterMsg(Reservation reservation);

    EmailMessage reservationOpenMsg(Employee admin, Reservation reservation);

    EmailMessage reservationApprovedMsg(Reservation reservation);

    EmailMessage applicationWorkReportMsg(String targetEmail, String jvmStartTimeStr,
                                          int relErrorLogsSize, int archErrorLogsSize,
                                          String memUsageStr, String clientHostErrorsPage,
                                                                         File attachment);
}
