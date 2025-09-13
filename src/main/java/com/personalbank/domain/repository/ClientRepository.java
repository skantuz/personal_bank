package com.personalbank.domain.repository;

import com.personalbank.domain.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {
    Client save(Client client);
    Optional<Client> findById(Long id);
    Optional<Client> findByEmail(String email);
    List<Client> findAll();
    List<Client> findByFirstNameContainingIgnoreCase(String firstName);
    List<Client> findByLastNameContainingIgnoreCase(String lastName);
    void deleteById(Long id);
    boolean existsByEmail(String email);
}