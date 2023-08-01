package com.josuevqz.users.usuarioslogin.repository;


import com.josuevqz.users.usuarioslogin.models.entity.Rol;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Rol, Long> {

    Optional<Rol> findByname(String name);


}
