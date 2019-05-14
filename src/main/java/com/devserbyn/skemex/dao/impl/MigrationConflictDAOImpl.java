package com.devserbyn.skemex.dao.impl;

import com.devserbyn.skemex.dao.MigrationConflictDAO;
import com.devserbyn.skemex.entity.MigrationConflict;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MigrationConflictDAOImpl extends AbstractDao<MigrationConflict, Long> implements MigrationConflictDAO {

    @Override
    public List<MigrationConflict> allNotResolvedConflicts(String nickname) {
        return sessionFactory.getCurrentSession()
                .createNamedQuery(MigrationConflict.ALL_NOT_RESOLVED, MigrationConflict.class)
                .setParameter("nickname", nickname).getResultList();
    }

    @Override
    public int allNotResolvedConflictsCount(String nickname) {
        return sessionFactory.getCurrentSession()
                .createNamedQuery(MigrationConflict.ALL_NOT_RESOLVED_COUNT, Number.class)
                .setParameter("nickname", nickname).getSingleResult().intValue();
    }
}
