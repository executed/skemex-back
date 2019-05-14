package com.devserbyn.skemex.dao.impl;

import com.devserbyn.skemex.dao.OrganizationDAO;
import com.devserbyn.skemex.utility.DaoUtility;
import com.devserbyn.skemex.entity.Organization;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class OrganizationDAOImpl extends AbstractDao<Organization, Long> implements OrganizationDAO {
    @Override
    public Optional<Organization> findByName(String name) {
        return DaoUtility.findOrEmpty(()-> ((Organization) createNamedQuery(Organization.FIND_BY_NAME)
                .setParameter("name", name).getSingleResult()));
    }

    @Override
    public boolean existsByName(String name) {
        return DaoUtility.findOrEmpty(()-> ((Organization) createNamedQuery(Organization.FIND_BY_NAME)
                .setParameter("name", name).getSingleResult())).isPresent();
    }
}
