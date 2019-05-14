package com.devserbyn.skemex.service;

import com.devserbyn.skemex.entity.Employee;
import com.devserbyn.skemex.entity.User;

import java.util.Optional;


public interface UserService {
    Optional<User> findById(String nickname);

    User save(User user);

    User createByEmployee(Employee employee);
}
