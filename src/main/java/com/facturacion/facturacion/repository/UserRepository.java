package com.facturacion.facturacion.repository;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.facturacion.facturacion.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
