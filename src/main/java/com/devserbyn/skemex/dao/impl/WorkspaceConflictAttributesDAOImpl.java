package com.devserbyn.skemex.dao.impl;

import com.devserbyn.skemex.dao.WorkspaceConflictAttributesDAO;
import com.devserbyn.skemex.entity.WorkspaceConflictAttributes;
import com.devserbyn.skemex.entity.WorkspaceStatus;
import org.springframework.stereotype.Repository;

@Repository
public class WorkspaceConflictAttributesDAOImpl extends AbstractDao<WorkspaceConflictAttributes, Long>
        implements WorkspaceConflictAttributesDAO {

    @Override
    public WorkspaceConflictAttributes findByWorkspaceOwnerStatus(String ownerNickname, Integer wsNumber, WorkspaceStatus wsStatus) {
        return sessionFactory.getCurrentSession()
                .createNamedQuery(WorkspaceConflictAttributes.FIND_BY_WORKSPACE_OWNER_STATUS, WorkspaceConflictAttributes.class)
                .setParameter("nickname", ownerNickname)
                .setParameter("workspace", wsNumber)
                .setParameter("status", wsStatus)
                .getResultList().stream().findFirst().orElse(null);
    }
}
