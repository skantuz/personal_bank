package com.personalbank.infrastructure.persistence;

import com.personalbank.domain.model.Account;
import com.personalbank.domain.model.Transaction;
import com.personalbank.domain.model.TransactionStatus;
import com.personalbank.domain.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaTransactionRepository extends JpaRepository<Transaction, Long> {
    
    Optional<Transaction> findByTransactionReference(String transactionReference);
    
    List<Transaction> findByAccount(Account account);
    
    List<Transaction> findByAccountId(Long accountId);
    
    List<Transaction> findByStatus(TransactionStatus status);
    
    List<Transaction> findByTransactionType(TransactionType transactionType);
    
    List<Transaction> findByAccountAndCreatedAtBetween(Account account, LocalDateTime startDate, LocalDateTime endDate);
    
    List<Transaction> findByAccountIdAndCreatedAtBetween(Long accountId, LocalDateTime startDate, LocalDateTime endDate);
    
    boolean existsByTransactionReference(String transactionReference);
    
    long countByAccount(Account account);
}