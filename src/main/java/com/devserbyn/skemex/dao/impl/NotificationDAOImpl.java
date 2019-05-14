package com.devserbyn.skemex.dao.impl;

import com.devserbyn.skemex.dao.NotificationDAO;
import com.devserbyn.skemex.entity.Notification;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
public class NotificationDAOImpl extends AbstractDao<Notification, Long> implements NotificationDAO {
    @Override
    @SuppressWarnings("unchecked")
    public List<Notification> allNotViewedNotification(String nickname) {
        return createNamedQuery(Notification.FIND_NOT_VIEWED_NOTIFICATION).setParameter("nickname", nickname).getResultList();
    }

    @Override
    public int allNotViewedNotificationCount(String nickname) {
        return ((Number)createNamedQuery(Notification.FIND_NOT_VIEWED_NOTIFICATION_COUNT).setParameter("nickname", nickname).getSingleResult()).intValue();
    }

    @Transactional
    @Override
    @SuppressWarnings("unchecked")
    public List<Notification> makeAllNotificationViewedByNickname(String nickname) {
        createNamedQueryUpdate(Notification.MAKE_ALL_NOTIFICATION_VIEWED_BY_NICKNAME).setParameter("nickname", nickname).executeUpdate();
        return new ArrayList<>();
    }
}
