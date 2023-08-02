package com.josuevqz.users.usuarioslogin.services;

import com.josuevqz.users.usuarioslogin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JPAUserDetailsServices implements UserDetailsService {
    @Autowired
    private UserRepository repository;
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.josuevqz.users.usuarioslogin.models.User> o = repository.findByUsername(username);
        if (!o.isPresent()){
            throw  new UsernameNotFoundException(String.format("Username %s no existe en el sistema",  username ));
        }
        com.josuevqz.users.usuarioslogin.models.User user = o.orElseThrow();

        List<GrantedAuthority>authorities = user.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());

        return new User(
                user.getUsername(),
                user.getPassword(),
                true,
                true,
                true,
                true,
                authorities);
    }
}
