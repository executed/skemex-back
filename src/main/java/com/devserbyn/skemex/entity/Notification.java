package com.devserbyn.skemex.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@NamedQueries({
        @NamedQuery(name = Notification.FIND_NOT_VIEWED_NOTIFICATION, query = "select n from Notification n where n.recipient.nickname = :nickname and " +
                "n.viewed = false order by n.type"),
        @NamedQuery(name = Notification.FIND_NOT_VIEWED_NOTIFICATION_COUNT, query = "select count(n) from Notification n where n.recipient.nickname = :nickname and " +
                "n.viewed = false"),
        @NamedQuery(name = Notification.MAKE_ALL_NOTIFICATION_VIEWED_BY_NICKNAME, query = "update Notification n set n.viewed = true where n.recipient.nickname = :nickname and " +
                "n.viewed = false")
})
@Table(name = "notification")
public class Notification {

    public static final String FIND_NOT_VIEWED_NOTIFICATION = "Notification.findNotViewed";
    public static final String FIND_NOT_VIEWED_NOTIFICATION_COUNT = "Notification.findNotViewedCount";
    public static final String MAKE_ALL_NOTIFICATION_VIEWED_BY_NICKNAME = "Notification.MakeAllViewedByNickname";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    Employee recipient;

    @Column
    private String message;

    @Column
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column
    private Boolean viewed = false;

    private Notification(Employee recipient, String message, NotificationType type) {
        this.recipient = recipient;
        this.message = message;
        this.type = type;
    }

    public static Notification info(Employee recipient, String message){
        return new Notification(recipient, message, NotificationType.INFO);
    }

    public static Notification warning(Employee recipient, String message){
        return new Notification(recipient, message, NotificationType.WARNING);
    }

    public static Notification error(Employee recipient, String message){
        return new Notification(recipient, message, NotificationType.ERROR);
    }

    public static Notification fatal(Employee recipient, String message){
        return new Notification(recipient, message, NotificationType.FATAL);
    }
}
