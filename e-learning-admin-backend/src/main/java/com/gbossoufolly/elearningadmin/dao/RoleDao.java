package com.gbossoufolly.elearningadmin.dao;

import com.gbossoufolly.elearningadmin.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDao extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
