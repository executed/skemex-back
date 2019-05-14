package com.devserbyn.skemex.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import liquibase.change.DatabaseChangeNote;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@NamedQuery(name = Office.FIND_BY_CITY, query = "select o from Office o where o.city = :city order by o.city")
public class Office {
    public static final String FIND_BY_CITY = "Office.findByCity";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @DatabaseChangeNote
    private Long id;

    @Column
    private String name;

    @Column
    private String city;

    @Column(name = "time_zone")
    private String timeZone;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE,mappedBy = "office")
    private RoomConflictAttributes roomConflictAttributes;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "office", cascade = CascadeType.REMOVE)
    private Set<Floor> floors;

}
