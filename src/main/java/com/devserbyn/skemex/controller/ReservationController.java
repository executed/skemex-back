package com.devserbyn.skemex.controller;

import com.devserbyn.skemex.controller.dto.Response;
import com.devserbyn.skemex.exception.NotFoundException;
import com.devserbyn.skemex.service.ReservationService;
import com.devserbyn.skemex.controller.dto.ReservationDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Response<ReservationDTO> addReservation(@RequestBody ReservationDTO reservationDTO) {
        return Response.success(reservationService.save(reservationDTO));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public Response<List<ReservationDTO>> listAllReservation() {
        List<ReservationDTO> reservation = reservationService.findAll();
        return Response.success(reservation);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public Response<ReservationDTO> findReservation(@PathVariable("id") Long id) {
        ReservationDTO reservation = reservationService.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Reservation with id: %d not found", id)));
        return Response.success(reservation);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping(value = "/workspace/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public Response<List<ReservationDTO>> findReservationByWorkspace(@PathVariable("id") Long id) {
        List<ReservationDTO> reservationDTOS = reservationService.reservationByWorkspace(id);
        return Response.success(reservationDTOS);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PostMapping(value = "/isAvailable")
    public Response<Boolean> isTimeForRequestAvailable(@RequestBody ReservationDTO dto) {
        return Response.success(reservationService.isTimeForReservationAvailable(dto.getStartTime(), dto.getEndTime(), dto.getWorkspaceId()));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PutMapping
    @ResponseStatus(code = HttpStatus.OK)
    public Response<ReservationDTO> updateReservation(@RequestBody ReservationDTO reservationDTO) {
        return Response.success(reservationService.update(reservationDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteReservation(@PathVariable("id") Long id) {
        reservationService.deleteById(id);
    }
}
