package com.facturacion.facturacion.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.facturacion.facturacion.dto.*;
import com.facturacion.facturacion.model.User;
import com.facturacion.facturacion.repository.UserRepository;
import com.facturacion.facturacion.security.JwtService;

@Service
public class AuthService {

    private final UserRepository repo;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository repo, JwtService jwtService) {
        this.repo = repo;
        this.jwtService = jwtService;
    }

    public void register(RegisterRequest req) {
        User user = new User();
        user.setUsername(req.username);
        user.setPassword(encoder.encode(req.password));
        user.setRole(req.role);
        if(repo.findByUsername(req.getUsername()).isPresent()){
            throw new RuntimeException("Username already exists");
        }

        repo.save(user);
    }

    public AuthResponse login(AuthRequest req) {
        User user = repo.findByUsername(req.username).orElseThrow();
        if (!encoder.matches(req.password, user.getPassword()))
            throw new RuntimeException("Credenciales incorrectas");

        return new AuthResponse(jwtService.generateToken(user.getUsername()));
    }
}
