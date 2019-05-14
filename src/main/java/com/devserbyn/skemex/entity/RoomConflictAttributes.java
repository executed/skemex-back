package com.devserbyn.skemex.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@NamedQueries({
        @NamedQuery(
                name = RoomConflictAttributes.FIND_BY_OFFICE_AND_ROOM,
                query = "select r from RoomConflictAttributes r where r.office.city = :office and r.title = :title and r.conflict.resolved = false"
        )
})
@Table(name = "\"roomconflictattributes\"")
public class RoomConflictAttributes {

    public static final String FIND_BY_OFFICE_AND_ROOM = "RoomConflictAttributes.findByOfficeAndRoom";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne
    MigrationConflict conflict;

    @JsonIgnore
    @OneToOne
    Office office;

    @Column
    String title;

    public RoomConflictAttributes(String title, Office office) {
        this.title = title;
        this.office = office;
    }
}
