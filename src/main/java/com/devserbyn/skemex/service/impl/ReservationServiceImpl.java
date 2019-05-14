package com.devserbyn.skemex.service.impl;

import com.devserbyn.skemex.entity.RequestStatus;
import com.devserbyn.skemex.entity.Reservation;
import com.devserbyn.skemex.entity.RoleName;
import com.devserbyn.skemex.mapping.ReservationMapper;
import com.devserbyn.skemex.service.ReservationService;
import com.devserbyn.skemex.controller.dto.ReservationDTO;
import com.devserbyn.skemex.controller.search.ReservationSearch;
import com.devserbyn.skemex.dao.EmployeeDAO;
import com.devserbyn.skemex.dao.ReservationDAO;
import com.devserbyn.skemex.dao.WorkspaceDAO;
import com.devserbyn.skemex.exception.BadRequestException;
import com.devserbyn.skemex.exception.NotFoundException;
import com.devserbyn.skemex.service.schedule.ReservationScheduleService;
import com.devserbyn.skemex.service.schedule.filter.ReservationUpdateFilter;
import com.devserbyn.skemex.utility.SecurityUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {
    private final ReservationDAO reservationDAO;
    private final EmployeeDAO employeeDAO;
    private final WorkspaceDAO workspaceDAO;
    private final ReservationMapper mapper;
    private final ReservationScheduleService reservationScheduleService;
    private final ReservationEmailService emailService;

    @Autowired
    public ReservationServiceImpl(ReservationDAO reservationDAO, EmployeeDAO employeeDAO,
                                  WorkspaceDAO workspaceDAO, ReservationMapper mapper,
                                  ReservationScheduleService reservationScheduleService,
                                  ReservationEmailService emailService) {
        this.reservationDAO = reservationDAO;
        this.employeeDAO = employeeDAO;
        this.workspaceDAO = workspaceDAO;
        this.mapper = mapper;
        this.reservationScheduleService = reservationScheduleService;
        this.emailService = emailService;
    }

    @Override
    public ReservationDTO save(ReservationDTO dto) {
        Reservation reservation;
        if (!isTimeForReservationAvailable(dto.getStartTime(), dto.getEndTime(), dto.getWorkspaceId()))
            throw new BadRequestException();
        if (SecurityUtility.hasRole(RoleName.ROLE_ADMIN)) {
            reservation = Reservation.builder().requestedTime(LocalDateTime.now()).requestStatus(RequestStatus.OPEN).startTime(dto.getStartTime())
                    .endTime(dto.getEndTime()).employee(employeeDAO.findById(dto.getEmployeeNickname()).orElseThrow(BadRequestException::new))
                    .workspace(workspaceDAO.findById(dto.getWorkspaceId()).orElseThrow(BadRequestException::new))
                    .description(dto.getDescription())
                    .build();
        } else {
            reservation = Reservation.builder().requestedTime(LocalDateTime.now()).requestStatus(RequestStatus.OPEN).startTime(dto.getStartTime())
                    .endTime(dto.getEndTime()).employee(employeeDAO.findById(SecurityUtility.getUsername()).orElseThrow(BadRequestException::new))
                    .workspace(workspaceDAO.findById(dto.getWorkspaceId()).orElseThrow(BadRequestException::new))
                    .build();
        }

        reservation.setRequester(employeeDAO.findById(SecurityUtility.getUsername()).orElseThrow(NotFoundException::new));

        return mapper.toDTO(reservationDAO.save(reservation));
    }

    @Override
    public boolean isTimeForReservationAvailable(LocalDate startTime, LocalDate endTime, Long workspaceId) {
        if (startTime.isBefore(LocalDate.now()))
            return false;
        List<Reservation> reservationByWorkspace = reservationDAO.findByParameters(
                ReservationSearch.builder().workspaceId(workspaceId).requestStatus(RequestStatus.APPROVED).build()
        );
        reservationByWorkspace.addAll(reservationDAO.findByParameters(ReservationSearch.builder().workspaceId(workspaceId)
                .requestStatus(RequestStatus.ACTIVE).build()));

        for (Reservation reservation : reservationByWorkspace) {
            if (isAvailable(reservation, startTime, endTime)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ReservationDTO update(ReservationDTO dto) {
        final Reservation updatedRes = reservationDAO.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException(String.format("Reservation with id: %s not found",
                        dto.getId())));
        updatedRes.setRequestStatus(dto.getRequestStatus());
        if (dto.getRequestStatus().equals(RequestStatus.APPROVED)) {
            List<Reservation> reservationByWorkspace = reservationDAO.findByWorkspace(dto.getWorkspaceId());
            for (Reservation reservation : reservationByWorkspace) {
                if (!dto.getId().equals(reservation.getId()) && isAvailable(reservation, dto.getStartTime(), dto.getEndTime())) {
                    reservation.setRequestStatus(RequestStatus.DECLINED);
                    reservationDAO.save(reservation);
                    emailService.sendReservationDeclinedEmail(reservation);
                }
            }
        }
        updatedRes.setApprover(employeeDAO.findById(SecurityUtility.getUsername()).orElseThrow(NotFoundException::new));
        Reservation reservation = reservationDAO.save(updatedRes);
        reservationScheduleService.checkReservation();

        return mapper.toDTO(reservation);
    }

    @Override
    public Optional<ReservationDTO> findById(Long id) {
        return reservationDAO.findById(id).map(mapper::toDTO);
    }

    @Override
    public List<ReservationDTO> findAll() {
        return mapper.toDTO(reservationDAO.findAll());
    }

    @Override
    public void deleteById(Long id) {
        reservationDAO.deleteById(id);
    }

    @Override
    public List<ReservationDTO> reservationByWorkspace(Long id) {
        return mapper.toDTO(reservationDAO.findByWorkspace(id));
    }

    @Override
    public List<Reservation> findByWorkspaceId(Long id) {
        return reservationDAO.findByWorkspace(id);
    }

    @Override
    public void updateReservations(ReservationUpdateFilter filter) {
        reservationDAO.updateReservations(filter);
    }

    @Override
    public boolean existsById(Long id) {
        return reservationDAO.existsById(id);
    }

    @Override
    public void delete(ReservationDTO dto) {
        reservationDAO.delete(mapper.toEntity(dto));
    }

    private boolean isAvailable(Reservation reservation, LocalDate startTime, LocalDate endTime) {
        return (reservation.getEndTime() == null && (endTime == null || !endTime.isBefore(reservation.getStartTime())))
                || (endTime == null && !reservation.getEndTime().isBefore(startTime))
                || ((endTime == null || !reservation.getStartTime().isAfter(endTime)) && !reservation.getStartTime().isBefore(startTime))
                || (endTime != null && reservation.getEndTime() != null && !reservation.getEndTime().isAfter(endTime) && !reservation.getEndTime().isBefore(startTime))
                || (endTime != null && reservation.getEndTime() != null && !reservation.getEndTime().isBefore(endTime) && !reservation.getStartTime().isAfter(startTime));
    }

}
