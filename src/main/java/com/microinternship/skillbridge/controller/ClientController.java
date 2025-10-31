package com.microinternship.skillbridge.controller;

import com.microinternship.skillbridge.entity.Client;
import com.microinternship.skillbridge.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    // ---------------- REGISTER ----------------
    @PostMapping
    public ResponseEntity<?> registerClient(@RequestBody Map<String, String> req) {
        String companyName = req.get("companyName");
        String email = req.get("email");
        String password = req.get("password");

        if (companyName == null || email == null || password == null) {
            return ResponseEntity.badRequest().body("Missing required fields");
        }

        Client client = new Client();
        client.setCompany(companyName); // Correct field
        client.setName(companyName);    // Optional, same as company
        client.setEmail(email);
        client.setPassword(password);

        Client saved = clientService.saveClient(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ---------------- LOGIN ----------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        Client client = clientService.login(email, password);
        if (client != null) {
            return ResponseEntity.ok(client);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    // ---------------- GET PROFILE ----------------
    @GetMapping("/{id}")
    public ResponseEntity<?> getClient(@PathVariable Long id) {
        return clientService.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Client not found"));
    }

    // ---------------- UPDATE PROFILE ----------------
    @PutMapping("/{id}")
    public ResponseEntity<?> updateClient(@PathVariable Long id, @RequestBody Client payload) {
        try {
            Client updated = clientService.updateClient(id, payload);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
