package com.personalbank.domain.repository;

import com.personalbank.domain.model.Account;
import com.personalbank.domain.model.AccountStatus;
import com.personalbank.domain.model.AccountType;
import com.personalbank.domain.model.Client;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    
    Account save(Account account);
    
    Optional<Account> findById(Long id);
    
    Optional<Account> findByAccountNumber(String accountNumber);
    
    List<Account> findAll();
    
    List<Account> findByClient(Client client);
    
    List<Account> findByClientId(Long clientId);
    
    List<Account> findByStatus(AccountStatus status);
    
    boolean existsByAccountNumber(String accountNumber);
    
    void deleteById(Long id);
    
    long count();
    
    long countByClient(Client client);
}