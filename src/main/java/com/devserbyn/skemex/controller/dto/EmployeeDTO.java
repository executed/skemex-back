package com.devserbyn.skemex.controller.dto;

import com.devserbyn.skemex.entity.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeDTO {
    private String nickname;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Set<Role> roles;
    private String organizationName;
    private Boolean active;
}
