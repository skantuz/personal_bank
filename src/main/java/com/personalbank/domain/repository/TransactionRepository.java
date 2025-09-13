package com.personalbank.domain.repository;

import com.personalbank.domain.model.Transaction;
import com.personalbank.domain.model.TransactionStatus;
import com.personalbank.domain.model.TransactionType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    Transaction save(Transaction transaction);
    Optional<Transaction> findById(Long id);
    Optional<Transaction> findByReferenceNumber(String referenceNumber);
    List<Transaction> findByAccountId(Long accountId);
    List<Transaction> findByAccountIdAndCreatedAtBetween(Long accountId, LocalDateTime startDate, LocalDateTime endDate);
    List<Transaction> findByStatus(TransactionStatus status);
    List<Transaction> findByTransactionType(TransactionType transactionType);
    List<Transaction> findAll();
    void deleteById(Long id);
}