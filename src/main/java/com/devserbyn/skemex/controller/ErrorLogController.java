package com.devserbyn.skemex.controller;

import com.devserbyn.skemex.controller.dto.ErrorLogDTO;
import com.devserbyn.skemex.controller.dto.Response;
import com.devserbyn.skemex.exception.NotFoundException;
import com.devserbyn.skemex.mapping.ErrorLogMapper;
import com.devserbyn.skemex.service.ErrorLogService;
import com.devserbyn.skemex.entity.ErrorLog;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/errors")
@RequiredArgsConstructor
public class ErrorLogController {

    private final ErrorLogService service;
    private final ErrorLogMapper mapper;

    @GetMapping
    public Response<List<ErrorLogDTO>> findAll() {
        List<ErrorLog> errorLogList = service.findAll();
        return Response.success(mapper.toDTO(errorLogList));
    }

    @GetMapping("/archived")
    public Response<List<ErrorLogDTO>> findAllArchived() {
        List<ErrorLog> errorLogList = service.findAllArchived();
        return Response.success(mapper.toDTO(errorLogList));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response<ErrorLog> createErrorLog(@Validated @RequestBody ErrorLog errorLog) {
        ErrorLog savedInstance = service.save(errorLog);
        return Response.success(savedInstance);
    }

    @GetMapping("/location")
    public Response<String> findLogLocationById(@NotNull @RequestParam Long id){
        String errorLogLocation = service.findLogLocationById(id)
                                         .orElseThrow(NotFoundException::new);
        return Response.success(errorLogLocation);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteErrorLog(@NotNull @PathVariable Long id){
        service.deleteById(id);
    }
}
