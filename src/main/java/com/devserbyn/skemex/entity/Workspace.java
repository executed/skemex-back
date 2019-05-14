package com.devserbyn.skemex.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries({
        @NamedQuery(name = Workspace.FIND_BY_ROOM_ID, query = "select ws from Workspace ws where ws.room.id = :id order by ws.number"),
        @NamedQuery(name = Workspace.FIND_BY_ROOM_ID_AND_STATUS_FREE, query = "select ws from Workspace ws where ws.room.id = :id and ws.status = 'FREE' order by ws.number"),
        @NamedQuery(name = Workspace.FIND_BY_STATUS, query = "select ws from Workspace ws where ws.status = :status order by ws.number"),
        @NamedQuery(name = Workspace.FIND_ALL_OK_FOR_ROOM, query = "SELECT w from Workspace w where w.status = 'FREE' and w not in " +
                "(select w from Workspace w join w.reservations res where res.requestStatus in ('ACTIVE','APPROVED') and w.status = 'FREE' " +
                "and res.endTime >= :startTime and res.endTime is not null) and w.room.id = :room")
})
public class Workspace {
    public static final String FIND_BY_ROOM_ID = "Workspace.findByIdRoom";
    public static final String FIND_BY_STATUS = "Workspace.findByStatus";
    public static final String FIND_ALL_OK_FOR_ROOM = "Workspace.findAllOkForRoom";
    public static final String FIND_BY_ROOM_ID_AND_STATUS_FREE = "Workspace.findByIdRoomAndStatusFree";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer number;

    @Enumerated(EnumType.STRING)
    private WorkspaceStatus status;

    @Column
    private Float x;

    @Column
    private Float y;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Room room;

    @ManyToOne(fetch = FetchType.EAGER)
    private Employee employee;

    @JsonIgnore
    @OneToMany(mappedBy = "workspace", cascade = CascadeType.REMOVE)
    private List<WorkspaceConflictAttributes> workspaceConflictAttributes;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "workspace", cascade = CascadeType.REMOVE)
    private List<Reservation> reservations;

}
