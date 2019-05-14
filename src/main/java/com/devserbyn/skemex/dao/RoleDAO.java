package com.devserbyn.skemex.dao;

import com.devserbyn.skemex.entity.Role;
import com.devserbyn.skemex.entity.RoleName;

import java.util.Optional;

public interface RoleDAO extends IBaseDAO<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}
