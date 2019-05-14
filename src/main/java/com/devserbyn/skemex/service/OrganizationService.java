package com.devserbyn.skemex.service;

import com.devserbyn.skemex.controller.dto.OrganizationDTO;
import com.devserbyn.skemex.entity.Organization;

import java.util.List;
import java.util.Optional;

public interface OrganizationService {
    Optional<OrganizationDTO> findById(Long id);

    List<OrganizationDTO> findAll();

    void deleteById(Long id);

    boolean existsByName(String name);

    Organization findByName(String name);

    Organization save(Organization organization);

}
