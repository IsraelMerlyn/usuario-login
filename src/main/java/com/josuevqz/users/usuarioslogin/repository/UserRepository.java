package com.josuevqz.users.usuarioslogin.repository;

import com.josuevqz.users.usuarioslogin.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);
    @Query("select u from User u where u.username=?1")
    Optional<User>getUserByUsername(String username);
}
