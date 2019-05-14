package com.devserbyn.skemex.dao.impl;

import com.devserbyn.skemex.dao.UserDAO;
import com.devserbyn.skemex.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAOImpl extends AbstractDao <User, String> implements UserDAO {
}
