package com.devserbyn.skemex.controller;

import com.devserbyn.skemex.exception.NotFoundException;
import com.devserbyn.skemex.service.WorkspaceService;
import com.devserbyn.skemex.controller.dto.Response;
import com.devserbyn.skemex.controller.dto.WorkspaceDTO;
import com.devserbyn.skemex.controller.search.WorkspaceFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workspaces")
public class WorkspaceController {
    private final WorkspaceService workspaceService;

    @Autowired
    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Response<List<WorkspaceDTO>> listAllByRoom(@RequestParam("roomId") Long roomId) {
        List<WorkspaceDTO> all = workspaceService.findAllByIdRoom(roomId);
        return Response.success(all);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Response<WorkspaceDTO> addWorkspace(@RequestBody WorkspaceDTO workspaceDTO) {
        return Response.success(workspaceService.save(workspaceDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Response<WorkspaceDTO> updateWorkspace(@RequestBody WorkspaceDTO workspaceDTO) {
        return Response.success(workspaceService.update(workspaceDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWorkspace(@PathVariable Long id) {
        workspaceService.deleteById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping(value = "/{id}")
    public Response<WorkspaceDTO> findById(@PathVariable("id") Long id) {
        return Response.success(workspaceService.findById(id).orElseThrow(() -> new NotFoundException(String.format("Workspace with id: %d not found", id))));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/filter")
    public Response<List<WorkspaceDTO>> filter(@RequestBody WorkspaceFilter workspaceFilter) {
        return Response.success(workspaceService.search(workspaceFilter));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/saveAll/{id}")
    public Response<List<WorkspaceDTO>> filter(@RequestBody List<WorkspaceDTO> workspaces, @PathVariable("id") Long roomId) {
        return Response.success(workspaceService.saveAll(workspaces, roomId));
    }
}
