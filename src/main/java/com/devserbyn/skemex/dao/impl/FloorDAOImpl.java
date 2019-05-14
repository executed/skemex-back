package com.devserbyn.skemex.dao.impl;

import com.devserbyn.skemex.dao.FloorDAO;
import com.devserbyn.skemex.entity.Floor;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FloorDAOImpl extends AbstractDao<Floor, Long> implements FloorDAO {

    @Override
    @SuppressWarnings("unchecked")
    public List<Floor> findAllFloorsByOfficeId(Long id) {
        return (List<Floor>)createNamedQuery(Floor.FIND_BY_OFFICE_ID)
                .setParameter("id", id)
                .getResultList();
    }
}
