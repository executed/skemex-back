package com.devserbyn.skemex.service.impl;

import com.devserbyn.skemex.mapping.NotificationMapper;
import com.devserbyn.skemex.service.NotificationService;
import com.devserbyn.skemex.controller.dto.NotificationDTO;
import com.devserbyn.skemex.dao.NotificationDAO;
import com.devserbyn.skemex.entity.Notification;
import com.devserbyn.skemex.exception.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {


    private final NotificationDAO notificationDAO;
    private final NotificationMapper mapper;

    @Autowired
    public NotificationServiceImpl(NotificationDAO notificationDAO, NotificationMapper mapper) {
        this.notificationDAO = notificationDAO;
        this.mapper = mapper;
    }

    @Override
    public Notification save(Notification notification) {
        return notificationDAO.save(notification);
    }

    @Override
    public List<NotificationDTO> findAllNotViewed(String nickname) {
        return mapper.toDTO(notificationDAO.allNotViewedNotification(nickname));
    }

    @Override
    public int countOfNotViewedNotifications(String nickname) {
        return notificationDAO.allNotViewedNotificationCount(nickname);
    }

    @Override
    public Optional<NotificationDTO> findById(Long id) {
        return notificationDAO.findById(id).map(mapper::toDTO);
    }

    @Override
    public NotificationDTO update(NotificationDTO notificationDTO) {
        final Notification updatedNotification = notificationDAO.findById(notificationDTO.getId())
                .orElseThrow(() -> new NotFoundException(String.format("Notification with id: %s not found",
                        notificationDTO.getId())));
        updatedNotification.setViewed(notificationDTO.getViewed());
        return mapper.toDTO(notificationDAO.save(updatedNotification));
    }

    @Override
    public List<NotificationDTO> makeAllViewedByNickname(String nickname) {
        return mapper.toDTO(notificationDAO.makeAllNotificationViewedByNickname(nickname));
    }
}
