package com.devserbyn.skemex.service.impl;

import com.devserbyn.skemex.mapping.FloorMapper;
import com.devserbyn.skemex.service.FloorService;
import com.devserbyn.skemex.controller.dto.FloorDTO;
import com.devserbyn.skemex.dao.FloorDAO;
import com.devserbyn.skemex.dao.OfficeDAO;
import com.devserbyn.skemex.entity.Floor;
import com.devserbyn.skemex.entity.Office;
import com.devserbyn.skemex.exception.BadRequestException;
import com.devserbyn.skemex.exception.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FloorServiceImpl implements FloorService {
    private final FloorDAO floorDAO;
    private final OfficeDAO officeDAO;
    private final FloorMapper mapper;

    @Autowired
    public FloorServiceImpl(FloorDAO floorDAO, OfficeDAO officeDAO, FloorMapper mapper) {
        this.floorDAO = floorDAO;
        this.officeDAO = officeDAO;
        this.mapper = mapper;
    }

    @Override
    public FloorDTO save(FloorDTO dto) {
        final Floor floorToSave = mapper.toEntity(dto);
        Office office = officeDAO.findById(dto.getOfficeId())
                .orElseThrow(() -> new NotFoundException(String.format("Office with id: %d not found",
                        dto.getOfficeId())));
        floorToSave.setOffice(office);
        if(existsFloorByOffice(dto.getNumber(), dto.getOfficeId())){
            return mapper.toDTO(floorDAO.save(floorToSave));
        }else {
            throw new BadRequestException("This floor is exist in this office");
        }
    }


    @Override
    public Optional<FloorDTO> findById(Long id) {
        return floorDAO.findById(id).map(mapper::toDTO);
    }

    @Override
    public List<FloorDTO> findAllFloorsByOfficeId(Long officeId) {
        return mapper.toDTO(floorDAO.findAllFloorsByOfficeId(officeId));
    }

    @Override
    public void deleteById(Long id) {
        floorDAO.deleteById(id);
    }

    @Override
    public boolean existsFloorByOffice(Integer floorNumber, Long officeId) {
        if(floorNumber == null){
            return false;
        }
        List<Floor> floors = floorDAO.findAllFloorsByOfficeId(officeId);
        for (Floor floor : floors) {
            if (floor.getNumber().equals(floorNumber)) {
                return false;
            }
        }
        return true;
    }
}
