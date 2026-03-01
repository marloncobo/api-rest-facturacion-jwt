package com.facturacion.facturacion.security;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import com.facturacion.facturacion.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repo;

    public CustomUserDetailsService(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        var user = repo.findByUsername(username).orElseThrow();
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}

