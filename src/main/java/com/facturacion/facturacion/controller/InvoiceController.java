package com.facturacion.facturacion.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.facturacion.facturacion.dto.InvoiceDTO;
import com.facturacion.facturacion.model.Invoice;
import com.facturacion.facturacion.service.InvoiceService;

@RestController
@RequestMapping("/invoices")

public class InvoiceController {

    private final InvoiceService service;

    public InvoiceController(InvoiceService service) {
        this.service = service;
    }

    @GetMapping
    public List<Invoice> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Invoice getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public Invoice create(@RequestBody InvoiceDTO dto) {
        return service.save(dto);
    }

    @PutMapping("/{id}")
    public Invoice update(@PathVariable Long id, @RequestBody InvoiceDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
