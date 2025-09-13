package com.personalbank.infrastructure.persistence;

import com.personalbank.domain.model.Account;
import com.personalbank.domain.model.Transaction;
import com.personalbank.domain.model.TransactionStatus;
import com.personalbank.domain.model.TransactionType;
import com.personalbank.domain.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class TransactionRepositoryAdapter implements TransactionRepository {
    
    private final JpaTransactionRepository jpaTransactionRepository;
    
    @Autowired
    public TransactionRepositoryAdapter(JpaTransactionRepository jpaTransactionRepository) {
        this.jpaTransactionRepository = jpaTransactionRepository;
    }
    
    @Override
    public Transaction save(Transaction transaction) {
        return jpaTransactionRepository.save(transaction);
    }
    
    @Override
    public Optional<Transaction> findById(Long id) {
        return jpaTransactionRepository.findById(id);
    }
    
    @Override
    public Optional<Transaction> findByTransactionReference(String transactionReference) {
        return jpaTransactionRepository.findByTransactionReference(transactionReference);
    }
    
    @Override
    public List<Transaction> findAll() {
        return jpaTransactionRepository.findAll();
    }
    
    @Override
    public List<Transaction> findByAccount(Account account) {
        return jpaTransactionRepository.findByAccount(account);
    }
    
    @Override
    public List<Transaction> findByAccountId(Long accountId) {
        return jpaTransactionRepository.findByAccountId(accountId);
    }
    
    @Override
    public List<Transaction> findByStatus(TransactionStatus status) {
        return jpaTransactionRepository.findByStatus(status);
    }
    
    @Override
    public List<Transaction> findByTransactionType(TransactionType transactionType) {
        return jpaTransactionRepository.findByTransactionType(transactionType);
    }
    
    @Override
    public List<Transaction> findByAccountAndCreatedAtBetween(Account account, LocalDateTime startDate, LocalDateTime endDate) {
        return jpaTransactionRepository.findByAccountAndCreatedAtBetween(account, startDate, endDate);
    }
    
    @Override
    public List<Transaction> findByAccountIdAndCreatedAtBetween(Long accountId, LocalDateTime startDate, LocalDateTime endDate) {
        return jpaTransactionRepository.findByAccountIdAndCreatedAtBetween(accountId, startDate, endDate);
    }
    
    @Override
    public boolean existsByTransactionReference(String transactionReference) {
        return jpaTransactionRepository.existsByTransactionReference(transactionReference);
    }
    
    @Override
    public void deleteById(Long id) {
        jpaTransactionRepository.deleteById(id);
    }
    
    @Override
    public long count() {
        return jpaTransactionRepository.count();
    }
    
    @Override
    public long countByAccount(Account account) {
        return jpaTransactionRepository.countByAccount(account);
    }
}