package com.devserbyn.skemex.service.impl;

import com.devserbyn.skemex.mapping.OrganizationMapper;
import com.devserbyn.skemex.service.OrganizationService;
import com.devserbyn.skemex.controller.dto.OrganizationDTO;
import com.devserbyn.skemex.dao.OrganizationDAO;
import com.devserbyn.skemex.entity.Organization;
import com.devserbyn.skemex.exception.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationDAO organizationDAO;
    private final OrganizationMapper mapper;

    @Autowired
    public OrganizationServiceImpl(OrganizationDAO organizationDAO, OrganizationMapper mapper) {
        this.organizationDAO = organizationDAO;
        this.mapper = mapper;
    }

    @Override
    public Optional<OrganizationDTO> findById(Long id) {
        return organizationDAO.findById(id).map(mapper::toDTO);
    }

    @Override
    public List<OrganizationDTO> findAll() {
        return mapper.toDTO(organizationDAO.findAll());
    }

    @Override
    public void deleteById(Long id) {
        organizationDAO.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return organizationDAO.existsByName(name);
    }

    @Override
    public Organization findByName(String name) {
        return organizationDAO.findByName(name).orElseThrow((() -> new NotFoundException("Organization doesn't exist")));
    }

    @Override
    public Organization save(Organization organization) {
        return organizationDAO.save(organization);
    }
}
