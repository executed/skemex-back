package com.devserbyn.skemex.service;

import com.devserbyn.skemex.entity.WorkspaceConflictAttributes;
import com.devserbyn.skemex.entity.WorkspaceStatus;

public interface WorkspaceConflictAttributesService {

    WorkspaceConflictAttributes save(WorkspaceConflictAttributes attr);

    WorkspaceConflictAttributes findByWorkspaceOwnerStatus(String ownerNickname, Integer wsNumber, WorkspaceStatus wsStatus);
}
