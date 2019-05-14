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
@NamedQuery(name = Organization.FIND_BY_NAME, query = "select org from Organization org where org.name = :name")
public class Organization {
    public static final String FIND_BY_NAME = "Organization.findByName";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinColumn(name = "parent", referencedColumnName = "id")
    private Organization parent;

    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    private Employee owner;

}
