package com.devserbyn.skemex.dao.impl;

import com.devserbyn.skemex.dao.OfficeDAO;
import com.devserbyn.skemex.utility.DaoUtility;
import com.devserbyn.skemex.entity.Office;

import org.springframework.stereotype.Repository;


@Repository
public class OfficeDAOImpl extends AbstractDao<Office, Long> implements OfficeDAO {

    @Override
    public boolean existsByCity(String city) {
        return DaoUtility.findOrEmpty(()-> ((Office) createNamedQuery(Office.FIND_BY_CITY)
                .setParameter("city", city).getSingleResult())).isPresent();
    }

    @Override
    public Office getByCity(String city) {
        return ((Office) createNamedQuery(Office.FIND_BY_CITY)
                .setParameter("city", city).getSingleResult());
    }
}
