package com.devserbyn.skemex.dao.impl;

import com.devserbyn.skemex.dao.RoomConflictAttributesDAO;
import com.devserbyn.skemex.entity.RoomConflictAttributes;
import org.springframework.stereotype.Repository;

@Repository
public class RoomConflictAttributesDAOImpl extends AbstractDao<RoomConflictAttributes, Long>
        implements RoomConflictAttributesDAO {

    @Override
    public RoomConflictAttributes findByOfficeAndRoom(String office, String roomTitle) {
        return sessionFactory.getCurrentSession()
                .createNamedQuery(RoomConflictAttributes.FIND_BY_OFFICE_AND_ROOM, RoomConflictAttributes.class)
                .setParameter("office", office)
                .setParameter("title", roomTitle)
                .getResultList().stream().findFirst().orElse(null);
    }
}
