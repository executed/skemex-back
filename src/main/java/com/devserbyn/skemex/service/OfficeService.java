package com.devserbyn.skemex.service;

import com.devserbyn.skemex.controller.dto.OfficeDTO;

import java.util.List;
import java.util.Optional;

public interface OfficeService {
    Optional<OfficeDTO> findById(Long id);

    List<OfficeDTO> findAll();

    boolean existsByCity(String city);

    OfficeDTO save(OfficeDTO dto);

    void deleteById(Long id);
}
