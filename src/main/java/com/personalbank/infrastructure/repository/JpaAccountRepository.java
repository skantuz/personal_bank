package com.personalbank.infrastructure.repository;

import com.personalbank.domain.model.Account;
import com.personalbank.domain.model.AccountStatus;
import com.personalbank.domain.model.AccountType;
import com.personalbank.domain.repository.AccountRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaAccountRepository extends JpaRepository<Account, Long>, AccountRepository {
    
    @Override
    Optional<Account> findByAccountNumber(String accountNumber);
    
    @Override
    List<Account> findByClientId(Long clientId);
    
    @Override
    List<Account> findByStatus(AccountStatus status);
    
    @Override
    List<Account> findByAccountType(AccountType accountType);
    
    @Override
    boolean existsByAccountNumber(String accountNumber);
}