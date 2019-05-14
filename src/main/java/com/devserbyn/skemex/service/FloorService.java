package com.devserbyn.skemex.service;

import com.devserbyn.skemex.controller.dto.FloorDTO;

import java.util.List;
import java.util.Optional;

public interface FloorService {
    FloorDTO save(FloorDTO dto);

    Optional<FloorDTO> findById(Long id);

    List<FloorDTO> findAllFloorsByOfficeId(Long officeId);

    void deleteById(Long id);

    boolean existsFloorByOffice(Integer floorNumber, Long officeId);
}
