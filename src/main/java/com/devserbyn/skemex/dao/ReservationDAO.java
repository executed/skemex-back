package com.devserbyn.skemex.dao;

import com.devserbyn.skemex.controller.search.ReservationSearch;
import com.devserbyn.skemex.service.schedule.filter.ReservationUpdateFilter;
import com.devserbyn.skemex.entity.Reservation;

import java.util.List;

public interface ReservationDAO extends IBaseDAO<Reservation, Long> {
    List<Reservation> findByWorkspace(Long id);

    List<Reservation> findByParameters(ReservationSearch reservationSearch);

    void updateReservations(ReservationUpdateFilter filter);
}
