package com.devserbyn.skemex.service;

import com.devserbyn.skemex.controller.dto.NotificationDTO;
import com.devserbyn.skemex.entity.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationService {
    Notification save(Notification notification);

    List<NotificationDTO> findAllNotViewed(String nickname);

    int countOfNotViewedNotifications(String nickname);

    Optional<NotificationDTO> findById(Long id);

    NotificationDTO update(NotificationDTO notificationDTO);

    List<NotificationDTO> makeAllViewedByNickname(String nickname);
}
