package com.devserbyn.skemex.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@NamedQueries({
        @NamedQuery(name = Room.FIND_BY_OFFICE_ID, query = "select r from Room r where r.floor.office.id = :id order by r.title"),
        @NamedQuery(name = Room.FIND_BY_FLOOR_ID, query = "select r from Room r where r.floor.id = :id order by r.title"),
        @NamedQuery(name = Room.FIND_BY_TITLE_AND_CITY, query = "SELECT r from Room r where r.title = :title and r.floor.office.city = :city "),
        @NamedQuery(name = Room.FIND_BY_START_TIME, query = "SELECT r from Room r join r.workspaces w where w.status = 'FREE' and w not in " +
                "(select w from Workspace w join w.reservations res where res.requestStatus in ('ACTIVE','APPROVED') and w.status = 'FREE' " +
                "and res.endTime >= :startTime and res.endTime is not null) " +
                "group by r,r.floor.number  having count(w) >= :countOfPlaces order by r.floor.number")
})
public class Room {
    public static final String FIND_BY_OFFICE_ID = "Room.findByOfficeId";
    public static final String FIND_BY_FLOOR_ID = "Room.findByFloorId";
    public static final String FIND_BY_TITLE_AND_CITY = "Room.findByTitleAndCity";
    public static final String FIND_BY_START_TIME = "Room.findByStartTime";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    private Floor floor;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "room", cascade = CascadeType.REMOVE)
    private Set<Workspace> workspaces;

    public long countSpaceLeft() {
        if(workspaces == null){
            return 0;
        }
        return this.workspaces.stream().map(Workspace::getStatus)
                .filter(s -> s == WorkspaceStatus.FREE)
                .count();
    }
}
