package com.facturacion.facturacion.service;


import java.util.List;
import org.springframework.stereotype.Service;
import com.facturacion.facturacion.model.Client;
import com.facturacion.facturacion.repository.ClientRepository;

@Service
public class ClientService {

    private final ClientRepository repo;

    public ClientService(ClientRepository repo) {
        this.repo = repo;
    }

    public List<Client> findAll() {
        return repo.findAll();
    }

    public Client findById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public Client save(Client client) {
        return repo.save(client);
    }

    public Client update(Long id, Client client) {
        Client existing = findById(id);
        existing.setName(client.getName());
        existing.setEmail(client.getEmail());
        return repo.save(existing);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
