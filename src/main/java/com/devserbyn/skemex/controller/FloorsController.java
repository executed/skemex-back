package com.devserbyn.skemex.controller;


import com.devserbyn.skemex.service.FloorService;
import com.devserbyn.skemex.controller.dto.FloorDTO;
import com.devserbyn.skemex.controller.dto.Response;;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
@RestController
@RequestMapping("/floors")
public class FloorsController {

    private FloorService floorService;

    @Autowired
    public FloorsController(FloorService floorService) {
        this.floorService = floorService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Response<List<FloorDTO>> getAllFloors(@RequestParam("officeId") Long officeId) {
        return Response.success(floorService.findAllFloorsByOfficeId(officeId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/isAvailable")
    public Response<Boolean> isFloorAvailable(@RequestParam("floorNumber")Integer floorNumber, @RequestParam("officeId")Long officeId){
        return Response.success(floorService.existsFloorByOffice(floorNumber, officeId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response<FloorDTO> addFloor(@RequestBody FloorDTO floorDTO) {
        return Response.success(floorService.save(floorDTO));
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFloor(@PathVariable Long id) {
        floorService.deleteById(id);
    }


}

