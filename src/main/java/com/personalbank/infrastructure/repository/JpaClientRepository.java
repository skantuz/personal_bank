package com.personalbank.infrastructure.repository;

import com.personalbank.domain.model.Client;
import com.personalbank.domain.repository.ClientRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaClientRepository extends JpaRepository<Client, Long>, ClientRepository {
    
    @Override
    Optional<Client> findByEmail(String email);
    
    @Override
    List<Client> findByFirstNameContainingIgnoreCase(String firstName);
    
    @Override
    List<Client> findByLastNameContainingIgnoreCase(String lastName);
    
    @Override
    boolean existsByEmail(String email);
}