package com.devserbyn.skemex.controller;

import com.devserbyn.skemex.exception.NotFoundException;
import com.devserbyn.skemex.service.OfficeService;
import com.devserbyn.skemex.controller.dto.OfficeDTO;
import com.devserbyn.skemex.controller.dto.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
@RestController
@RequestMapping(value = "/offices")
public class OfficeController {

    private final OfficeService officeService;

    @Autowired
    public OfficeController(OfficeService officeService) {
        this.officeService = officeService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Response<List<OfficeDTO>> listAllOffices() {
        return Response.success(officeService.findAll());
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<OfficeDTO> findOffice(@PathVariable("id") Long id) {
        OfficeDTO office = officeService.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Office with id: %d not found", id)));
        return Response.success(office);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response<OfficeDTO> addOffice(@RequestBody OfficeDTO officeDTO){
        return Response.success(officeService.save(officeDTO));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoom(@PathVariable Long id){
        officeService.deleteById(id);
    }
}
