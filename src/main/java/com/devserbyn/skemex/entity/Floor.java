package com.devserbyn.skemex.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@NamedQuery(name = Floor.FIND_BY_OFFICE_ID, query = "select f from Floor f where f.office.id = :id order by f.number DESC")
public class Floor {
    public static final String FIND_BY_OFFICE_ID = "Floor.findByOfficeId";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer number;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "floor", cascade = CascadeType.REMOVE)
    private Set<Room> rooms;

    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    private Office office;

}
