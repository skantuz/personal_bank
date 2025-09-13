package com.personalbank.infrastructure.repository;

import com.personalbank.domain.model.Transaction;
import com.personalbank.domain.model.TransactionStatus;
import com.personalbank.domain.model.TransactionType;
import com.personalbank.domain.repository.TransactionRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaTransactionRepository extends JpaRepository<Transaction, Long>, TransactionRepository {
    
    @Override
    Optional<Transaction> findByReferenceNumber(String referenceNumber);
    
    @Override
    List<Transaction> findByAccountId(Long accountId);
    
    @Override
    List<Transaction> findByAccountIdAndCreatedAtBetween(Long accountId, LocalDateTime startDate, LocalDateTime endDate);
    
    @Override
    List<Transaction> findByStatus(TransactionStatus status);
    
    @Override
    List<Transaction> findByTransactionType(TransactionType transactionType);
}