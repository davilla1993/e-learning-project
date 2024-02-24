package com.gbossoufolly.elearningadmin.services;

import com.gbossoufolly.elearningadmin.models.User;

public interface UserService {

    User loadUserByEmail(String email);

    User createUser(String email, String password);

    void assignRoleToUser(String email, String roleName);


}
