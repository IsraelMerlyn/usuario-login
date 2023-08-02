package com.josuevqz.users.usuarioslogin.services;

import com.josuevqz.users.usuarioslogin.models.User;
import com.josuevqz.users.usuarioslogin.models.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserServices{
    List<UserDTO> findAll();
    Optional<UserDTO> findByID(Long id);
    UserDTO save(User user);
    Optional<UserDTO> update(User user, Long id);

    void  remove(Long id);
}
