package com.personalbank.domain.repository;

import com.personalbank.domain.model.Account;
import com.personalbank.domain.model.Transaction;
import com.personalbank.domain.model.TransactionStatus;
import com.personalbank.domain.model.TransactionType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    
    Transaction save(Transaction transaction);
    
    Optional<Transaction> findById(Long id);
    
    Optional<Transaction> findByTransactionReference(String transactionReference);
    
    List<Transaction> findAll();
    
    List<Transaction> findByAccount(Account account);
    
    List<Transaction> findByAccountId(Long accountId);
    
    List<Transaction> findByStatus(TransactionStatus status);
    
    List<Transaction> findByTransactionType(TransactionType transactionType);
    
    List<Transaction> findByAccountAndCreatedAtBetween(Account account, LocalDateTime startDate, LocalDateTime endDate);
    
    List<Transaction> findByAccountIdAndCreatedAtBetween(Long accountId, LocalDateTime startDate, LocalDateTime endDate);
    
    boolean existsByTransactionReference(String transactionReference);
    
    void deleteById(Long id);
    
    long count();
    
    long countByAccount(Account account);
}