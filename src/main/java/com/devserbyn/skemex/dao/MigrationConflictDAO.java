package com.devserbyn.skemex.dao;

import com.devserbyn.skemex.entity.MigrationConflict;

import java.util.List;

public interface MigrationConflictDAO extends IBaseDAO<MigrationConflict, Long> {

    List<MigrationConflict> allNotResolvedConflicts(String nickname);

    int allNotResolvedConflictsCount(String nickname);
}
