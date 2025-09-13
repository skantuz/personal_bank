package com.personalbank.domain.repository;

import com.personalbank.domain.model.Client;
import com.personalbank.domain.model.ClientStatus;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {
    
    Client save(Client client);
    
    Optional<Client> findById(Long id);
    
    Optional<Client> findByEmail(String email);
    
    List<Client> findAll();
    
    List<Client> findByStatus(ClientStatus status);
    
    List<Client> findByFirstNameContainingIgnoreCase(String firstName);
    
    List<Client> findByLastNameContainingIgnoreCase(String lastName);
    
    boolean existsByEmail(String email);
    
    void deleteById(Long id);
    
    long count();
}