package com.devserbyn.skemex.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries ({
        @NamedQuery(name = Employee.FIND_BY_ORGANIZATIONS,
                query = "select e from Employee e where e.organization.id = :id order by e.nickname"),
        @NamedQuery(name = Employee.FIND_ADMIN_BY_OFFICE,
                query = "SELECT e FROM Employee e, User u JOIN u.roles r WHERE r.name = " +
                        "'ROLE_ADMIN' AND u.nickname = e.nickname AND e.organization.id = " +
                        ":organization")
})
public class Employee {

    public static final String FIND_BY_ORGANIZATIONS = "Employee.findByOrganization";
    public static final String FIND_ADMIN_BY_OFFICE = "Employee.findAdminByOrganization";

    @Id
    private String nickname;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column
    private String email;

    @ManyToOne(fetch = FetchType.EAGER)
    private Organization organization;

    @Column
    private Boolean active = true;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    private List<Workspace> workspaces;

}
