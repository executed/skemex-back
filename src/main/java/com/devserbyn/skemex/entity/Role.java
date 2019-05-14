package com.devserbyn.skemex.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@NamedQuery(name = Role.FIND_BY_NAME, query = "select r from Role r where r.name = :name")
public class Role {
    public static final String FIND_BY_NAME = "Role.findByName";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private RoleName name;

}
