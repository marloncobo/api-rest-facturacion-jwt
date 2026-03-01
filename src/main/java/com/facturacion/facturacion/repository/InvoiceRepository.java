package com.facturacion.facturacion.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.facturacion.facturacion.model.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
