package com.devserbyn.skemex.dao.impl;

import com.devserbyn.skemex.dao.RoleDAO;
import com.devserbyn.skemex.utility.DaoUtility;
import com.devserbyn.skemex.entity.Role;
import com.devserbyn.skemex.entity.RoleName;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RoleDAOImpl extends AbstractDao<Role, Long> implements RoleDAO {

    @Override
    public Optional<Role> findByName(RoleName roleName) {
        return DaoUtility.findOrEmpty(()-> ((Role) createNamedQuery(Role.FIND_BY_NAME)
                .setParameter("name", roleName).getSingleResult()));
    }
}
