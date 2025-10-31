package com.microinternship.skillbridge.service;

import com.microinternship.skillbridge.entity.Client;
import com.microinternship.skillbridge.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // ---------- Basic CRUD ----------

    /** Get all clients */
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    /** Register / save a new client */
    public Client saveClient(Client client) {
        if (client.getCreatedAt() == null) {
            client.setCreatedAt(LocalDateTime.now());
        }
        client.setUpdatedAt(LocalDateTime.now());
        if (client.getName() == null || client.getName().isEmpty()) {
            client.setName(client.getCompany());
        }
        return clientRepository.save(client);
    }

    /** Login client by email & password */
    public Client login(String email, String password) {
        return clientRepository.findByEmail(email)
                .filter(c -> c.getPassword().equals(password))
                .orElse(null);
    }

    /** Find client by id */
    public Optional<Client> findById(Long id) {
        return clientRepository.findById(id);
    }

    // ---------- Profile Update ----------

    public Client updateClient(Long id, Client payload) {
        return clientRepository.findById(id).map(existing -> {

            // Update only fields that exist in payload
            if (payload.getCompany() != null) existing.setCompany(payload.getCompany());
            if (payload.getName() != null) existing.setName(payload.getName());
            if (payload.getPhone() != null) existing.setPhone(payload.getPhone());
            if (payload.getAddress() != null) existing.setAddress(payload.getAddress());
            if (payload.getContactPerson() != null) existing.setContactPerson(payload.getContactPerson());
            if (payload.getCompanyDetails() != null) existing.setCompanyDetails(payload.getCompanyDetails());

            // Optional: update email/password if explicitly provided
            if (payload.getEmail() != null) existing.setEmail(payload.getEmail());
            if (payload.getPassword() != null && !payload.getPassword().isEmpty()) {
                existing.setPassword(payload.getPassword());
            }

            existing.setUpdatedAt(LocalDateTime.now());
            return clientRepository.save(existing);

        }).orElseThrow(() -> new RuntimeException("Client not found with id " + id));
    }
}
