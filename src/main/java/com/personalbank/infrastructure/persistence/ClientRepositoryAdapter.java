package com.personalbank.infrastructure.persistence;

import com.personalbank.domain.model.Client;
import com.personalbank.domain.model.ClientStatus;
import com.personalbank.domain.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ClientRepositoryAdapter implements ClientRepository {
    
    private final JpaClientRepository jpaClientRepository;
    
    @Autowired
    public ClientRepositoryAdapter(JpaClientRepository jpaClientRepository) {
        this.jpaClientRepository = jpaClientRepository;
    }
    
    @Override
    public Client save(Client client) {
        return jpaClientRepository.save(client);
    }
    
    @Override
    public Optional<Client> findById(Long id) {
        return jpaClientRepository.findById(id);
    }
    
    @Override
    public Optional<Client> findByEmail(String email) {
        return jpaClientRepository.findByEmail(email);
    }
    
    @Override
    public List<Client> findAll() {
        return jpaClientRepository.findAll();
    }
    
    @Override
    public List<Client> findByStatus(ClientStatus status) {
        return jpaClientRepository.findByStatus(status);
    }
    
    @Override
    public List<Client> findByFirstNameContainingIgnoreCase(String firstName) {
        return jpaClientRepository.findByFirstNameContainingIgnoreCase(firstName);
    }
    
    @Override
    public List<Client> findByLastNameContainingIgnoreCase(String lastName) {
        return jpaClientRepository.findByLastNameContainingIgnoreCase(lastName);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return jpaClientRepository.existsByEmail(email);
    }
    
    @Override
    public void deleteById(Long id) {
        jpaClientRepository.deleteById(id);
    }
    
    @Override
    public long count() {
        return jpaClientRepository.count();
    }
}