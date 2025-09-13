package com.personalbank.domain.repository;

import com.personalbank.domain.model.Account;
import com.personalbank.domain.model.AccountStatus;
import com.personalbank.domain.model.AccountType;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    Account save(Account account);
    Optional<Account> findById(Long id);
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByClientId(Long clientId);
    List<Account> findByStatus(AccountStatus status);
    List<Account> findByAccountType(AccountType accountType);
    List<Account> findAll();
    void deleteById(Long id);
    boolean existsByAccountNumber(String accountNumber);
}