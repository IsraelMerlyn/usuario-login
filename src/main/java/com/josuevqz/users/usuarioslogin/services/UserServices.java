package com.josuevqz.users.usuarioslogin.services;

import com.josuevqz.users.usuarioslogin.models.User;

import java.util.List;
import java.util.Optional;

public interface UserServices{
    List<User> findAll();
    Optional<User> findByID(Long id);
    User save(User user);

    void  remove(Long id);
}
