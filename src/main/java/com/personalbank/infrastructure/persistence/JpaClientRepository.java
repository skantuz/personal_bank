package com.personalbank.infrastructure.persistence;

import com.personalbank.domain.model.Client;
import com.personalbank.domain.model.ClientStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaClientRepository extends JpaRepository<Client, Long> {
    
    Optional<Client> findByEmail(String email);
    
    List<Client> findByStatus(ClientStatus status);
    
    List<Client> findByFirstNameContainingIgnoreCase(String firstName);
    
    List<Client> findByLastNameContainingIgnoreCase(String lastName);
    
    boolean existsByEmail(String email);
}