package com.devserbyn.skemex.service.impl;

import com.devserbyn.skemex.service.OfficeService;
import com.devserbyn.skemex.controller.dto.OfficeDTO;
import com.devserbyn.skemex.dao.OfficeDAO;
import com.devserbyn.skemex.entity.Office;
import com.devserbyn.skemex.mapping.OfficeMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OfficeServiceImpl implements OfficeService {

    private final OfficeDAO officeDAO;
    private final OfficeMapper mapper;


    @Autowired
    public OfficeServiceImpl(OfficeDAO officeDAO, OfficeMapper mapper) {
        this.officeDAO = officeDAO;
        this.mapper = mapper;
    }

    @Override
    public Optional<OfficeDTO> findById(Long id) {
        return officeDAO.findById(id).map(mapper::toDTO);
    }

    @Override
    public List<OfficeDTO> findAll() {
        return mapper.toDTO(officeDAO.findAll());
    }

    @Override
    public boolean existsByCity(String city) {
        return officeDAO.existsByCity(city);
    }

    @Override
    public OfficeDTO save(OfficeDTO dto) {
        final Office officeToSave = mapper.toEntity(dto);
        return mapper.toDTO(officeDAO.save(officeToSave));
    }

    @Override
    public void deleteById(Long id) {
        officeDAO.deleteById(id);
    }
}
