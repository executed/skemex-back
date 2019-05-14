package com.devserbyn.skemex.service;

import com.devserbyn.skemex.controller.dto.RoomDTO;
import com.devserbyn.skemex.controller.search.RoomFilter;
import com.devserbyn.skemex.entity.Room;

import java.util.List;
import java.util.Optional;

public interface RoomService {

    RoomDTO save(RoomDTO dto);

    RoomDTO update(RoomDTO dto);

    Optional<RoomDTO> findById(Long id);

    List<RoomDTO> findAllByFloorId(Long id);

    List<RoomDTO> findAllByOffice(Long id);

    List<RoomDTO> findAll();

    void deleteById(Long id);

    boolean existsByTitleAndCity(String title, String city);

    Room findByTitleAndCity(String title, String city);

    List<RoomDTO> search(RoomFilter roomFilter);
}
