package com.devserbyn.skemex.controller;

import com.devserbyn.skemex.exception.NotFoundException;
import com.devserbyn.skemex.service.OrganizationService;
import com.devserbyn.skemex.controller.dto.OrganizationDTO;
import com.devserbyn.skemex.controller.dto.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public Response<List<OrganizationDTO>> listAllOrganizations() {
        List<OrganizationDTO> organizations = organizationService.findAll();
        return Response.success(organizations);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public Response<OrganizationDTO> findOrganization(@PathVariable("id") Long id) {
        OrganizationDTO organization = organizationService.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Organization with id: %d not found",
                        id)));
        return Response.success(organization);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteOrganization(@PathVariable("id") Long id) {
        organizationService.deleteById(id);
    }
}
