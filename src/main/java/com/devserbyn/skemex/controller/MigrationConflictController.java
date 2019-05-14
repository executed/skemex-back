package com.devserbyn.skemex.controller;

import com.devserbyn.skemex.exception.BadRequestException;
import com.devserbyn.skemex.exception.NotFoundException;
import com.devserbyn.skemex.service.MigrationConflictService;
import com.devserbyn.skemex.controller.dto.MigrationConflictDTO;
import com.devserbyn.skemex.controller.dto.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/migrationConflicts")
public class MigrationConflictController {

    private final MigrationConflictService migrationConflictService;

    @Autowired
    public MigrationConflictController(MigrationConflictService migrationConflictService) {
        this.migrationConflictService = migrationConflictService;
    }

    @GetMapping(value = "/byEmployee/{nickname}/allNotViewed")
    public Response<List<MigrationConflictDTO>> getAllNotViewed(
            @PathVariable("nickname") String nickname
    ) {
        return Response.success(migrationConflictService.findAllNotResolvedConflicts(nickname));
    }

    @GetMapping(value = "/byEmployee/{nickname}/allNotViewedCount")
    public Response<Integer> getAllNotViewedCount(
            @PathVariable("nickname") String nickname
    ) {
        return Response.success(migrationConflictService.getAllNotResolvedCount(nickname));
    }

    @PostMapping("/resolveId/{id}/accept/{source}")
    public ResponseEntity resolveConflict(@PathVariable("source") String source,
                                          @PathVariable("id") Long conflictId,
                                          @RequestBody MigrationConflictDTO migrationConflictDTO) {

        if (source == null || !(source.equals("excel") || source.equals("db"))) {
            throw new BadRequestException("Incorrect data source");
        }

        boolean result = migrationConflictService.resolveConflict(source, conflictId, migrationConflictDTO);
        HttpStatus status = result ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity(status);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<MigrationConflictDTO> getById(@PathVariable("id") Long id) {
        return Response.success(migrationConflictService.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("MigrationConflict with id: %d not found", id))));
    }

    @PutMapping
    @ResponseStatus(code = HttpStatus.OK)
    public Response<MigrationConflictDTO> updateMigrationConflict(@RequestBody MigrationConflictDTO migrationConflictDTO){
        return Response.success(migrationConflictService.update(migrationConflictDTO));
    }
}
