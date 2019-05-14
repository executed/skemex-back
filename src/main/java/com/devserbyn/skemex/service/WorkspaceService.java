package com.devserbyn.skemex.service;

import com.devserbyn.skemex.controller.dto.WorkspaceDTO;
import com.devserbyn.skemex.controller.search.WorkspaceFilter;

import java.util.List;
import java.util.Optional;


public interface WorkspaceService {
    WorkspaceDTO save(WorkspaceDTO dto);

    WorkspaceDTO update(WorkspaceDTO update);

    Optional<WorkspaceDTO> findById(Long id);

    List<WorkspaceDTO> findAllByIdRoom(Long id);

    boolean existsById(Long id);

    void delete(WorkspaceDTO workspaceDTO);

    void deleteById(Long id);

    List<WorkspaceDTO> search(WorkspaceFilter workspaceFilter);

    List<WorkspaceDTO> saveAll(List<WorkspaceDTO> workspaces, Long roomId);
}
