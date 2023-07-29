package com.josuevqz.users.usuarioslogin.repository;

import com.josuevqz.users.usuarioslogin.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
