package com.devserbyn.skemex.dao;

import com.devserbyn.skemex.entity.Room;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomDAO extends IBaseDAO<Room, Long> {

    List<Room> findAllByFloor(Long id);

    List<Room> findAllByOffice(Long id);

    boolean existsByTitleAndCity(String title, String city);

    Optional<Room> findByTitleAndCity(String title, String city);

    List<Room> findFreeAfterStartTime(LocalDate startTime, Long count);
}
