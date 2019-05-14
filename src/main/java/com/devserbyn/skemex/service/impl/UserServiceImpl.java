package com.devserbyn.skemex.service.impl;

import com.devserbyn.skemex.service.UserService;
import com.devserbyn.skemex.configuration.security.UserDetailsImpl;
import com.devserbyn.skemex.dao.RoleDAO;
import com.devserbyn.skemex.dao.UserDAO;
import com.devserbyn.skemex.entity.Employee;
import com.devserbyn.skemex.entity.Role;
import com.devserbyn.skemex.entity.RoleName;
import com.devserbyn.skemex.entity.User;
import com.devserbyn.skemex.exception.BadRequestException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {
    private UserDAO userDAO;
    private PasswordEncoder passwordEncoder;
    private RoleDAO roleDAO;


    @Autowired
    public UserServiceImpl(UserDAO userDAO,
                           PasswordEncoder passwordEncoder,
                           RoleDAO roleDAO) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
        this.roleDAO = roleDAO;
    }

    @Override
    public Optional<User> findById(String nickname) {
        return userDAO.findById(nickname);
    }

    @Override
    public User save(User user) {
        return userDAO.save(user);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public User createByEmployee(Employee employee) {
        User user = new User();
        user.setNickname(employee.getNickname());
        user.setPassword(passwordEncoder
                .encode(employee.getFirstName().concat(employee.getLastName())));
        Set<Role> roles = new HashSet<>();
        roles.add(roleDAO
                .findByName(RoleName.ROLE_EMPLOYEE)
                .orElseThrow(() -> new BadRequestException(String.format("Role with name: %s doesn't exists",
                        RoleName.ROLE_EMPLOYEE.name()))));
        user.setRoles(roles);
        return userDAO.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        User user = findById(nickname)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with nickname: %s not found!", nickname)));
        return UserDetailsImpl.create(user);
    }
}
