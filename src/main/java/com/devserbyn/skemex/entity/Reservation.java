package com.devserbyn.skemex.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@NamedQueries({
        @NamedQuery(name = Reservation.FIND_APPLICABLE, query = "select r from Reservation r where r.workspace.id = :id and " +
                "r.requestStatus in ('ACTIVE','OPEN', 'APPROVED' ) order by r.startTime")
})
public class Reservation {

    public static final String FIND_APPLICABLE = "Reservation.findApplicable";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "requested_time")
    private LocalDateTime requestedTime;

    @Column(name = "start_time")
    private LocalDate startTime;

    @Column(name = "end_time")
    private LocalDate endTime;

    @Column(name = "request_status")
    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    @ManyToOne
    private Employee requester;

    @ManyToOne
    private Employee approver;

    @ManyToOne
    private Employee employee;

    @ManyToOne(fetch=FetchType.LAZY)
    private Workspace workspace;

    @Column
    private String description;
}
