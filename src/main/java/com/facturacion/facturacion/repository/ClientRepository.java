package com.facturacion.facturacion.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.facturacion.facturacion.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
