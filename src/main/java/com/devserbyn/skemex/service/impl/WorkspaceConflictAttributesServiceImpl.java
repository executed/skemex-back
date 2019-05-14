package com.devserbyn.skemex.service.impl;

import com.devserbyn.skemex.service.WorkspaceConflictAttributesService;
import com.devserbyn.skemex.dao.WorkspaceConflictAttributesDAO;
import com.devserbyn.skemex.entity.WorkspaceConflictAttributes;
import com.devserbyn.skemex.entity.WorkspaceStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class WorkspaceConflictAttributesServiceImpl implements WorkspaceConflictAttributesService {

    private final WorkspaceConflictAttributesDAO attributeDAO;

    @Autowired
    public WorkspaceConflictAttributesServiceImpl(final WorkspaceConflictAttributesDAO attributeDAO) {
        this.attributeDAO = attributeDAO;
    }

    @Override
    public WorkspaceConflictAttributes save(WorkspaceConflictAttributes attr) {
        return attributeDAO.save(attr);
    }

    @Override
    public WorkspaceConflictAttributes findByWorkspaceOwnerStatus(String ownerNickname, Integer wsNumber, WorkspaceStatus wsStatus) {
        return attributeDAO.findByWorkspaceOwnerStatus(ownerNickname, wsNumber, wsStatus);
    }
}
