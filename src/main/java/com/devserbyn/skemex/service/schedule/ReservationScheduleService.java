package com.devserbyn.skemex.service.schedule;

import com.devserbyn.skemex.entity.Employee;
import com.devserbyn.skemex.entity.RequestStatus;
import com.devserbyn.skemex.entity.Reservation;
import com.devserbyn.skemex.entity.Workspace;
import com.devserbyn.skemex.entity.WorkspaceStatus;
import com.devserbyn.skemex.service.EmployeeService;
import com.devserbyn.skemex.service.impl.ReservationEmailService;
import com.devserbyn.skemex.controller.search.ReservationSearch;
import com.devserbyn.skemex.dao.ReservationDAO;
import com.devserbyn.skemex.dao.WorkspaceDAO;
import com.devserbyn.skemex.exception.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional
public class ReservationScheduleService {

    private final ReservationEmailService emailService;
    private final ReservationDAO reservationDAO;
    private final WorkspaceDAO workspaceDAO;
    private final EmployeeService employeeService;

    @Autowired
    public ReservationScheduleService(ReservationDAO reservationDAO, WorkspaceDAO workspaceDAO,
                                      ReservationEmailService emailService, EmployeeService employeeService) {
        this.reservationDAO = reservationDAO;
        this.workspaceDAO = workspaceDAO;
        this.emailService = emailService;
        this.employeeService = employeeService;
    }

    @Scheduled(cron = CronSchedule.EVERYDAY_MIDNIGHT)
    public void init() {
        checkReservation();
        checkReservationOpen();
    }

    public void checkReservation() {

        ReservationSearch search1 =
                ReservationSearch.builder().startTime(LocalDate.now()).requestStatus(RequestStatus.APPROVED).build();
        List<Reservation> reservations = reservationDAO.findByParameters(search1);
        Stream<Reservation> resWithEndTimeStream = reservations.stream().filter(e -> e.getEndTime() != null);
        resWithEndTimeStream.forEach(e -> {
            e.setRequestStatus(RequestStatus.ACTIVE);
            emailService.sendReservationApprovedEmail(e);
        });

        reservations.stream().filter(e -> e.getEndTime() == null).forEach(e -> {
            Employee employee = e.getEmployee();
            List<Workspace> workspaces = employee.getWorkspaces();
            for (Workspace workspace1 : workspaces) {
                workspace1.setEmployee(null);
                workspace1.setStatus(WorkspaceStatus.FREE);
                workspaceDAO.save(workspace1);
            }
            Workspace workspace = workspaceDAO.findById(e.getWorkspace().getId()).orElseThrow((() -> new NotFoundException("Workspace doesn't exist")));
            workspace.setEmployee(e.getEmployee());
            workspace.setStatus(WorkspaceStatus.BUSY);
            e.setRequestStatus(RequestStatus.CLOSED);
            workspaceDAO.save(workspace);
            emailService.sendReservationApprovedEmail(e);
        });
        reservations.forEach(reservationDAO::save);

        ReservationSearch search2 = ReservationSearch.builder().endTime(LocalDate.now().minusDays(1)).requestStatus(RequestStatus.ACTIVE).build();
        List<Reservation> reservations2 = reservationDAO.findByParameters(search2);
        reservations2.forEach(e -> e.setRequestStatus(RequestStatus.CLOSED));
        reservations2.forEach(reservationDAO::save);
        emailService.sendReservationExpiredEmails(reservations2);

        ReservationSearch search3 = ReservationSearch.builder().startTime(LocalDate.now().minusDays(1)).requestStatus(RequestStatus.OPEN).build();
        List<Reservation> reservations3 = reservationDAO.findByParameters(search3);
        reservations3.forEach(e -> e.setRequestStatus(RequestStatus.DECLINED));
        reservations3.forEach(reservationDAO::save);
    }

    public void checkReservationOpen() {
        ReservationSearch openedReservationSearch = ReservationSearch.builder().endTime(LocalDate.now())
                .requestStatus(RequestStatus.OPEN)
                .build();
        List<Reservation> reservations = reservationDAO.findByParameters(openedReservationSearch);
        for (Reservation reservation : reservations) {
            long reservationOfficeId = reservation.getRequester().getOrganization().getId();
            List<Employee> adminList =
                    employeeService.findAllAdminsByOffice(reservationOfficeId);
            emailService.sendReservationOpenEmails(reservation, adminList);
        }
    }
}
