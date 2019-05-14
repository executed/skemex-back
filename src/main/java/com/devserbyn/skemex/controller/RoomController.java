package com.devserbyn.skemex.controller;

import com.devserbyn.skemex.exception.NotFoundException;
import com.devserbyn.skemex.service.RoomService;
import com.devserbyn.skemex.controller.dto.Response;
import com.devserbyn.skemex.controller.dto.RoomDTO;
import com.devserbyn.skemex.controller.search.RoomFilter;
import com.devserbyn.skemex.validation.dto.DTOValidationCheck.UpdateCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping
    public Response<List<RoomDTO>> listAllRooms() {
        List<RoomDTO> reservation = roomService.findAll();
        return Response.success(reservation);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/{id}")
    public Response<RoomDTO> findRoom(@NotNull @PathVariable("id") long id) {
        RoomDTO room = roomService.findById(id)
                .orElseThrow(NotFoundException::new);
        return Response.success(room);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response<RoomDTO> addRoom(@Validated @RequestBody RoomDTO roomDTO){
        return Response.success(roomService.save(roomDTO));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/byFloor")
    public Response<List<RoomDTO>> listAllByFloorId(@NotNull @RequestParam("floorId") Long id){
        return Response.success(roomService.findAllByFloorId(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/byOffice")
    public Response<List<RoomDTO>> listAllByOffice(@NotNull @RequestParam("officeId") Long id){
        return Response.success(roomService.findAllByOffice(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public Response<RoomDTO> updateRoom(@Validated(UpdateCheck.class)
                                        @RequestBody RoomDTO roomDTO){
        return Response.success(roomService.update(roomDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoom(@NotNull @PathVariable Long id){
        roomService.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/filter")
    public Response<List<RoomDTO>> searchRooms(@RequestBody RoomFilter roomFilter){
        return Response.success(roomService.search(roomFilter));
    }
}
