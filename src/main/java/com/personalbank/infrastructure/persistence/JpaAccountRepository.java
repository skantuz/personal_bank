package com.personalbank.infrastructure.persistence;

import com.personalbank.domain.model.Account;
import com.personalbank.domain.model.AccountStatus;
import com.personalbank.domain.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaAccountRepository extends JpaRepository<Account, Long> {
    
    Optional<Account> findByAccountNumber(String accountNumber);
    
    List<Account> findByClient(Client client);
    
    List<Account> findByClientId(Long clientId);
    
    List<Account> findByStatus(AccountStatus status);
    
    boolean existsByAccountNumber(String accountNumber);
    
    long countByClient(Client client);
}