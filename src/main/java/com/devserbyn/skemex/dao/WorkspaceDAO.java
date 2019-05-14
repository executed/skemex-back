package com.devserbyn.skemex.dao;

import com.devserbyn.skemex.entity.Workspace;
import com.devserbyn.skemex.entity.WorkspaceStatus;

import java.time.LocalDate;
import java.util.List;

public interface WorkspaceDAO extends IBaseDAO<Workspace, Long> {
    List<Workspace> findAllByIdRoom(Long id);

    List<Workspace> findAllByIdRoomAnsStatusFree(Long id);

    List<Workspace> findAllByStatus(WorkspaceStatus status);

    List<Workspace> findFreeAfterStartTime(LocalDate startDate, long roomId);


}
