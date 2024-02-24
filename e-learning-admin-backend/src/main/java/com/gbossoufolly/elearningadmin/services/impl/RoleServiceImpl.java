package com.gbossoufolly.elearningadmin.services.impl;

import com.gbossoufolly.elearningadmin.dao.RoleDao;
import com.gbossoufolly.elearningadmin.models.Role;
import com.gbossoufolly.elearningadmin.services.RoleService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public Role createRole(String roleName) {
        return roleDao.save(new Role(roleName));
    }
}
