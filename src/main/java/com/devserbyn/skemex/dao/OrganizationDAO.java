package com.devserbyn.skemex.dao;

import com.devserbyn.skemex.entity.Organization;

import java.util.Optional;

public interface OrganizationDAO extends IBaseDAO<Organization, Long> {
    Optional<Organization> findByName(String name);

    boolean existsByName(String name);
}
