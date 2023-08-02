package com.josuevqz.users.usuarioslogin.services;

import com.josuevqz.users.usuarioslogin.models.User;
import com.josuevqz.users.usuarioslogin.models.dto.UserDTO;
import com.josuevqz.users.usuarioslogin.models.dto.mapper.UserDtoMapper;
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
import java.util.stream.Collectors;

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
    public List<UserDTO> findAll() {
        List<User> users = (List<User>) repository.findAll();
        return users.stream().map(u -> UserDtoMapper.builder().setUser(u).build()).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> findByID(Long id) {
        return repository.findById(id).map(u -> UserDtoMapper.builder().setUser(u).build());

    }

    @Override
    @Transactional
    public UserDTO save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Optional<Rol> o = roleRepository.findByname("ROLE_USER");
        List<Rol> roles = new ArrayList<>();
        if (o.isPresent()){
            roles.add(o.orElseThrow());

        }
        user.setRoles(roles);
        return UserDtoMapper.builder().setUser(repository.save(user)).build();
    }

    @Override
    @Transactional
    public Optional<UserDTO> update(User user, Long id) {
        Optional<User> o = repository.findById(id);
        User userOptional = null;
        if (o.isPresent()){
            User userDB = o.orElseThrow();
            userDB.setUsername(user.getUsername());
            userDB.setEmail(user.getEmail());
            userOptional = repository.save(userDB);
        }
        return Optional.ofNullable(UserDtoMapper.builder().setUser(userOptional).build());
    }

    @Override
    @Transactional
    public void remove(Long id) {
        repository.deleteById(id);
    }
}
