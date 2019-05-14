package com.devserbyn.skemex.service.impl;

import com.devserbyn.skemex.service.FloorService;
import com.devserbyn.skemex.service.MigrationConflictService;
import com.devserbyn.skemex.service.RoomService;
import com.devserbyn.skemex.service.WorkspaceService;
import com.devserbyn.skemex.controller.dto.FloorDTO;
import com.devserbyn.skemex.controller.dto.MigrationConflictDTO;
import com.devserbyn.skemex.controller.dto.RoomDTO;
import com.devserbyn.skemex.controller.dto.WorkspaceDTO;
import com.devserbyn.skemex.dao.MigrationConflictDAO;
import com.devserbyn.skemex.entity.MigrationConflict;
import com.devserbyn.skemex.entity.RoomConflictAttributes;
import com.devserbyn.skemex.entity.WorkspaceConflictAttributes;
import com.devserbyn.skemex.exception.NotFoundException;
import com.devserbyn.skemex.mapping.MigrationConflictMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MigrationConflictServiceImpl implements MigrationConflictService {

    private final MigrationConflictDAO migrationConflictDAO;
    private final MigrationConflictMapper conflictMapper;
    private final WorkspaceService workspaceService;
    private final RoomService roomService;
    private final FloorService floorService;

    @Autowired
    public MigrationConflictServiceImpl(
            MigrationConflictDAO migrationConflictDAO,
            MigrationConflictMapper conflictMapper,
            WorkspaceService workspaceService,
            RoomService roomService, FloorService floorService) {
        this.migrationConflictDAO = migrationConflictDAO;
        this.conflictMapper = conflictMapper;
        this.workspaceService = workspaceService;
        this.roomService = roomService;
        this.floorService = floorService;
    }

    @Override
    public List<MigrationConflictDTO> findAllNotResolvedConflicts(String nickname) {
        return conflictMapper.toDTO(migrationConflictDAO.allNotResolvedConflicts(nickname));
    }

    @Override
    public Integer getAllNotResolvedCount(String nickname) {
        return migrationConflictDAO.allNotResolvedConflictsCount(nickname);
    }

    @Override
    public boolean resolveConflict(String source, Long id, MigrationConflictDTO migrationConflictDTO) {

        MigrationConflict conflict = migrationConflictDAO.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Conflict with id %d not found in database", id)));

        if (conflict.getResolved()) {
            return true;
        }

        boolean resolved = false;

        if (source.equals("excel")) {
            if (conflict.getRoomAttributes() != null) {
                resolved = acceptExcelRoom(conflict.getRoomAttributes(), migrationConflictDTO.getFloorNumber());
            }
            else if (conflict.getWorkspaceAttributes() != null) {
                resolved = acceptExcelWorkspace(conflict.getWorkspaceAttributes());
            }
        }
        else {
            resolved = true;
        }

        conflict.setResolved(resolved);
        migrationConflictDAO.save(conflict);

        return resolved;
    }

    private boolean acceptExcelRoom(RoomConflictAttributes roomAttributes, Integer floorNumber) {
        List<FloorDTO> floors = floorService.findAllFloorsByOfficeId(roomAttributes.getOffice().getId());
        long floorId = -1;
        for (FloorDTO floor: floors) {
            if (floor.getNumber().equals(floorNumber)) {
                floorId = floor.getId();
            }
        }
        if (floorId == -1) {
            throw new NotFoundException(String.format("Floor number %s not found", floorNumber));
        }

        RoomDTO dto = new RoomDTO();
        dto.setTitle(roomAttributes.getTitle());
        dto.setOfficeId(roomAttributes.getOffice().getId());
        dto.setFloorId(floorId);

        roomService.save(dto);
        return true;
    }

    private boolean acceptExcelWorkspace(WorkspaceConflictAttributes workspaceAttributes) {
        WorkspaceDTO workspace =
                    workspaceService.findById(workspaceAttributes.getWorkspace().getId()).orElseThrow(() -> new NotFoundException(
                            String.format("Conflict with id %d not found in database", workspaceAttributes.getWorkspace().getId())));

        workspace.setNickname(workspaceAttributes.getNewOwner().getNickname());
        workspace.setStatus(workspaceAttributes.getNewStatus());

        workspaceService.update(workspace);
        return true;
    }

    @Override
    public MigrationConflict save(MigrationConflict migrationConflict) {
        return migrationConflictDAO.save(migrationConflict);
    }

    @Override
    public Optional<MigrationConflictDTO> findById(Long id) {
        return migrationConflictDAO.findById(id).map(conflictMapper::toDTO);
    }

    @Override
    public MigrationConflictDTO update(MigrationConflictDTO migrationConflictDTO) {
        final MigrationConflict updatedMigrationConflict = migrationConflictDAO.findById(migrationConflictDTO.getId())
                .orElseThrow(() -> new NotFoundException(String.format("MigrationConflict with id: %s not found",
                        migrationConflictDTO.getId())));
        updatedMigrationConflict.setResolved(migrationConflictDTO.getResolved());
        return conflictMapper.toDTO(migrationConflictDAO.save(updatedMigrationConflict));
    }
}
