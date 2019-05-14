package com.devserbyn.skemex.service;

import com.devserbyn.skemex.controller.dto.MigrationConflictDTO;
import com.devserbyn.skemex.entity.MigrationConflict;

import java.util.List;
import java.util.Optional;

public interface MigrationConflictService {
    List<MigrationConflictDTO> findAllNotResolvedConflicts(String nickname);

    Integer getAllNotResolvedCount(String nickname);

    boolean resolveConflict(String source, Long id, MigrationConflictDTO migrationConflictDTO);

    MigrationConflict save(MigrationConflict migrationConflict);

    Optional<MigrationConflictDTO> findById(Long id);

    MigrationConflictDTO update(MigrationConflictDTO migrationConflictDTO);

}
