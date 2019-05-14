package com.devserbyn.skemex.dao;

import com.devserbyn.skemex.entity.WorkspaceConflictAttributes;
import com.devserbyn.skemex.entity.WorkspaceStatus;

public interface WorkspaceConflictAttributesDAO extends IBaseDAO<WorkspaceConflictAttributes, Long> {

    WorkspaceConflictAttributes findByWorkspaceOwnerStatus(String ownerNickname, Integer wsNumber, WorkspaceStatus wsStatus);
}
