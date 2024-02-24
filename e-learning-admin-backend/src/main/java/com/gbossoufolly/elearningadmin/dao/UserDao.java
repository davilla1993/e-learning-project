package com.gbossoufolly.elearningadmin.dao;

import com.gbossoufolly.elearningadmin.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Long>{
    User findByEmail(String email);
}
