package com.gbossoufolly.elearningadmin.services.impl;

import com.gbossoufolly.elearningadmin.dao.RoleDao;
import com.gbossoufolly.elearningadmin.dao.UserDao;
import com.gbossoufolly.elearningadmin.models.Role;
import com.gbossoufolly.elearningadmin.models.User;
import com.gbossoufolly.elearningadmin.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final RoleDao roleDao;

    public UserServiceImpl(UserDao userDao, RoleDao roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    @Override
    public User loadUserByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public User createUser(String email, String password) {
        return userDao.save(new User(email, password));
    }

    @Override
    public void assignRoleToUser(String email, String roleName) {
        User user = userDao.findByEmail(email);
        Role role = roleDao.findByName(roleName);
        user.assignRoleToUser(role);
    }
}
