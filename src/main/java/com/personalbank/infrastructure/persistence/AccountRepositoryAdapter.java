package com.personalbank.infrastructure.persistence;

import com.personalbank.domain.model.Account;
import com.personalbank.domain.model.AccountStatus;
import com.personalbank.domain.model.Client;
import com.personalbank.domain.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AccountRepositoryAdapter implements AccountRepository {
    
    private final JpaAccountRepository jpaAccountRepository;
    
    @Autowired
    public AccountRepositoryAdapter(JpaAccountRepository jpaAccountRepository) {
        this.jpaAccountRepository = jpaAccountRepository;
    }
    
    @Override
    public Account save(Account account) {
        return jpaAccountRepository.save(account);
    }
    
    @Override
    public Optional<Account> findById(Long id) {
        return jpaAccountRepository.findById(id);
    }
    
    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return jpaAccountRepository.findByAccountNumber(accountNumber);
    }
    
    @Override
    public List<Account> findAll() {
        return jpaAccountRepository.findAll();
    }
    
    @Override
    public List<Account> findByClient(Client client) {
        return jpaAccountRepository.findByClient(client);
    }
    
    @Override
    public List<Account> findByClientId(Long clientId) {
        return jpaAccountRepository.findByClientId(clientId);
    }
    
    @Override
    public List<Account> findByStatus(AccountStatus status) {
        return jpaAccountRepository.findByStatus(status);
    }
    
    @Override
    public boolean existsByAccountNumber(String accountNumber) {
        return jpaAccountRepository.existsByAccountNumber(accountNumber);
    }
    
    @Override
    public void deleteById(Long id) {
        jpaAccountRepository.deleteById(id);
    }
    
    @Override
    public long count() {
        return jpaAccountRepository.count();
    }
    
    @Override
    public long countByClient(Client client) {
        return jpaAccountRepository.countByClient(client);
    }
}