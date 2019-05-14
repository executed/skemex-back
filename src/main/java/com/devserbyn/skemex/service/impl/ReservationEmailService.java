package com.devserbyn.skemex.service.impl;

import com.devserbyn.skemex.service.EmailService;
import com.devserbyn.skemex.controller.dto.EmailMessage;
import com.devserbyn.skemex.entity.Employee;
import com.devserbyn.skemex.entity.Reservation;
import com.devserbyn.skemex.service.EmailMessageService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReservationEmailService {

    private final EmailService emailService;
    private final EmailMessageService emailMsgService;

    public void sendReservationExpiredEmails(List<Reservation> reservations) {
        Map<String, List<Reservation>> approverEmailAndReservationMap = new HashMap<>();
        // Sending emails to requesters, which reservation is expired
        for (Reservation reservation: reservations) {
            emailService.sendEmail(emailMsgService.expiringRequesterMsg(reservation));
            checkAndAddIfApproverInMap(approverEmailAndReservationMap, reservation);
        }
        // Sending emails to approvers with approved by them expired reservations
        for (Map.Entry<String, List<Reservation>> entry: approverEmailAndReservationMap.entrySet()) {
            String approverName = entry.getValue().get(0).getApprover().getFirstName();
            EmailMessage mail = emailMsgService.expiringApproverMsg(entry.getKey(), approverName,
                                                                                entry.getValue());
            emailService.sendEmail(mail);
        }
    }

    /** Checks whether approver of reservation was already add to map,
     *  if true - adds one more reservation to its list, false - creates new list with reservation
     */
    private void checkAndAddIfApproverInMap(Map<String,List<Reservation>> map, Reservation reservation) {
        String approverEmail = reservation.getApprover().getEmail();
        if (map.containsKey(approverEmail)){
            map.get(approverEmail).add(reservation);
        } else {
            List<Reservation> reservationList = new ArrayList<>();
            reservationList.add(reservation);
            map.put(approverEmail, reservationList);
        }
    }

    public void sendReservationOpenEmails(Reservation reservation, List<Employee> adminList) {
        adminList.forEach(a -> {
            emailService.sendEmail(emailMsgService.reservationOpenMsg(a, reservation));
        });
    }

    public void sendReservationApprovedEmail(Reservation reservation) {
            EmailMessage emailMessage = emailMsgService.reservationApprovedMsg(reservation);
            emailService.sendEmail(emailMessage);
    }

    public void sendReservationDeclinedEmail(Reservation reservation) {
        EmailMessage emailMessage = emailMsgService.reservationDeclinedMsg(reservation);
        emailService.sendEmail(emailMessage);
    }
}
