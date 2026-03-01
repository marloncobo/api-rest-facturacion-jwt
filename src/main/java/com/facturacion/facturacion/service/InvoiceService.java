package com.facturacion.facturacion.service;


import java.util.List;
import org.springframework.stereotype.Service;
import com.facturacion.facturacion.dto.InvoiceDTO;
import com.facturacion.facturacion.model.Client;
import com.facturacion.facturacion.model.Invoice;
import com.facturacion.facturacion.repository.ClientRepository;
import com.facturacion.facturacion.repository.InvoiceRepository;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepo;
    private final ClientRepository clientRepo;

    public InvoiceService(InvoiceRepository invoiceRepo, ClientRepository clientRepo) {
        this.invoiceRepo = invoiceRepo;
        this.clientRepo = clientRepo;
    }

    public List<Invoice> findAll() {
        return invoiceRepo.findAll();
    }

    public Invoice findById(Long id) {
        return invoiceRepo.findById(id).orElseThrow();
    }

    public Invoice save(InvoiceDTO dto) {
        Client client = clientRepo.findById(dto.clientId).orElseThrow();

        Invoice invoice = new Invoice();
        invoice.setDescription(dto.description);
        invoice.setTotal(dto.total);
        invoice.setClient(client);

        return invoiceRepo.save(invoice);
    }

    public Invoice update(Long id, InvoiceDTO dto) {
        Invoice invoice = findById(id);
        Client client = clientRepo.findById(dto.clientId).orElseThrow();

        invoice.setDescription(dto.description);
        invoice.setTotal(dto.total);
        invoice.setClient(client);

        return invoiceRepo.save(invoice);
    }

    public void delete(Long id) {
        invoiceRepo.deleteById(id);
    }
}
