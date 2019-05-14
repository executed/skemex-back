package com.devserbyn.skemex.entity;

import org.springframework.security.core.GrantedAuthority;

public enum RoleName implements GrantedAuthority {
    ROLE_ADMIN,
    ROLE_EMPLOYEE;




    @Override
    public String getAuthority() {
        return name();
    }
}
