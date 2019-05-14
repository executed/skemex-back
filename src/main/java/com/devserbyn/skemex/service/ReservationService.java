package com.devserbyn.skemex.service;

import com.devserbyn.skemex.controller.dto.ReservationDTO;
import com.devserbyn.skemex.entity.Reservation;
import com.devserbyn.skemex.service.schedule.filter.ReservationUpdateFilter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationService {
    ReservationDTO save(ReservationDTO dto);

    boolean isTimeForReservationAvailable(LocalDate startTime, LocalDate endTime, Long workspaceId);

    ReservationDTO update(ReservationDTO dto);

    Optional<ReservationDTO> findById(Long id);

    List<ReservationDTO> findAll();

    boolean existsById(Long id);

    void delete(ReservationDTO dto);

    void deleteById(Long id);

    List<ReservationDTO> reservationByWorkspace(Long id);

    List<Reservation> findByWorkspaceId(Long id);

    void updateReservations(ReservationUpdateFilter filter);
}
