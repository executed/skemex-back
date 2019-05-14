package com.devserbyn.skemex.dao;

import com.devserbyn.skemex.entity.Notification;

import java.util.List;

public interface NotificationDAO extends IBaseDAO<Notification, Long>{
    List<Notification> allNotViewedNotification(String nickname);

    int allNotViewedNotificationCount(String nickname);

    List<Notification> makeAllNotificationViewedByNickname(String nickname);
}
