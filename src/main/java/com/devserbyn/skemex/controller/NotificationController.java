package com.devserbyn.skemex.controller;

import com.devserbyn.skemex.exception.NotFoundException;
import com.devserbyn.skemex.service.NotificationService;
import com.devserbyn.skemex.controller.dto.NotificationDTO;
import com.devserbyn.skemex.controller.dto.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping(value = "/byEmployee/notificationsCount/{nickname}")
    @ResponseStatus(HttpStatus.OK)
    public Response<Integer> getAllNotViewedNotificationCount(@PathVariable("nickname") String nickname) {
        return Response.success(notificationService.countOfNotViewedNotifications(nickname));
    }

    @GetMapping(value = "/byEmployee/{nickname}")
    @ResponseStatus(HttpStatus.OK)
    public Response<List<NotificationDTO>> getAllNotViewedNotification(@PathVariable("nickname") String nickname) {
        return Response.success(notificationService.findAllNotViewed(nickname));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PutMapping(value = "/byEmployee/{nickname}")
    @ResponseStatus(HttpStatus.OK)
    public Response<List<NotificationDTO>> updateAllNotViewedNotification(@PathVariable("nickname") String nickname) {
        return Response.success(notificationService.makeAllViewedByNickname(nickname));
    }


    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response<NotificationDTO> getById(@PathVariable("id") Long id) {
        return Response.success(notificationService.findById(id).orElseThrow(() -> new NotFoundException(String.format("Notification with id: %d not found", id))));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PutMapping
    @ResponseStatus(code = HttpStatus.OK)
    public Response<NotificationDTO> updateNotification(@RequestBody NotificationDTO notificationDTO){
        return Response.success(notificationService.update(notificationDTO));
    }
}
