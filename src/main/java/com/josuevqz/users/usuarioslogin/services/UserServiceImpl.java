package com.josuevqz.users.usuarioslogin.services;

import com.josuevqz.users.usuarioslogin.models.User;
import com.josuevqz.users.usuarioslogin.models.entity.Rol;
import com.josuevqz.users.usuarioslogin.repository.RoleRepository;
import com.josuevqz.users.usuarioslogin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class UserServiceImpl implements  UserServices{
    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;
    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByID(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Optional<Rol> o = roleRepository.findByname("ROLE_USER");
        List<Rol> roles = new ArrayList<>();
        if (o.isPresent()){
            roles.add(o.orElseThrow());

        }
        user.setRoles(roles);
        return repository.save(user);
    }

    @Override
    @Transactional
    public Optional<User> update(User user, Long id) {
        Optional<User> o = this.findByID(id);
        User userOptional = null;
        if (o.isPresent()){
            User userDB = o.orElseThrow();
            userDB.setUsername(user.getUsername());
            userDB.setEmail(user.getEmail());
            userOptional=this.save(userDB);
        }
        return Optional.ofNullable(userOptional);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        repository.deleteById(id);
    }
}
