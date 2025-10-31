package com.microinternship.skillbridge.repository;

import com.microinternship.skillbridge.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    // This is the method Spring Data JPA needs to find a client by email
    Optional<Client> findByEmail(String email);
}
