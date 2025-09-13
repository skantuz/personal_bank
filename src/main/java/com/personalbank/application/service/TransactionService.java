package com.personalbank.application.service;

import com.personalbank.application.dto.TransactionDto;
import com.personalbank.domain.model.*;
import com.personalbank.domain.repository.AccountRepository;
import com.personalbank.domain.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public TransactionDto deposit(Long accountId, BigDecimal amount, String description) {
        Account account = getActiveAccount(accountId);
        
        Transaction transaction = new Transaction(TransactionType.DEPOSIT, amount, account);
        transaction.setDescription(description);
        transaction.setReferenceNumber(generateReferenceNumber());
        
        try {
            account.deposit(amount);
            transaction.complete(account.getBalance());
            
            accountRepository.save(account);
            Transaction savedTransaction = transactionRepository.save(transaction);
            
            return mapToDto(savedTransaction);
        } catch (Exception e) {
            transaction.fail();
            transactionRepository.save(transaction);
            throw new RuntimeException("Deposit failed: " + e.getMessage(), e);
        }
    }

    public TransactionDto withdraw(Long accountId, BigDecimal amount, String description) {
        Account account = getActiveAccount(accountId);
        
        Transaction transaction = new Transaction(TransactionType.WITHDRAWAL, amount, account);
        transaction.setDescription(description);
        transaction.setReferenceNumber(generateReferenceNumber());
        
        try {
            account.withdraw(amount);
            transaction.complete(account.getBalance());
            
            accountRepository.save(account);
            Transaction savedTransaction = transactionRepository.save(transaction);
            
            return mapToDto(savedTransaction);
        } catch (Exception e) {
            transaction.fail();
            transactionRepository.save(transaction);
            throw new RuntimeException("Withdrawal failed: " + e.getMessage(), e);
        }
    }

    public TransactionDto transfer(Long fromAccountId, Long toAccountId, BigDecimal amount, String description) {
        Account fromAccount = getActiveAccount(fromAccountId);
        Account toAccount = getActiveAccount(toAccountId);
        
        if (fromAccountId.equals(toAccountId)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        
        // Create transfer transaction for source account
        Transaction debitTransaction = new Transaction(TransactionType.TRANSFER, amount, fromAccount, toAccount, description);
        debitTransaction.setReferenceNumber(generateReferenceNumber());
        
        // Create transfer transaction for destination account  
        Transaction creditTransaction = new Transaction(TransactionType.TRANSFER, amount, toAccount, fromAccount, description);
        creditTransaction.setReferenceNumber(generateReferenceNumber());
        
        try {
            // Perform the transfer
            fromAccount.withdraw(amount);
            toAccount.deposit(amount);
            
            debitTransaction.complete(fromAccount.getBalance());
            creditTransaction.complete(toAccount.getBalance());
            
            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);
            
            transactionRepository.save(debitTransaction);
            transactionRepository.save(creditTransaction);
            
            return mapToDto(debitTransaction);
        } catch (Exception e) {
            debitTransaction.fail();
            creditTransaction.fail();
            transactionRepository.save(debitTransaction);
            transactionRepository.save(creditTransaction);
            throw new RuntimeException("Transfer failed: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public Optional<TransactionDto> getTransactionById(Long id) {
        return transactionRepository.findById(id).map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public Optional<TransactionDto> getTransactionByReferenceNumber(String referenceNumber) {
        return transactionRepository.findByReferenceNumber(referenceNumber).map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public List<TransactionDto> getTransactionsByAccountId(Long accountId) {
        return transactionRepository.findByAccountId(accountId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TransactionDto> getTransactionsByAccountIdAndDateRange(Long accountId, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByAccountIdAndCreatedAtBetween(accountId, startDate, endDate).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TransactionDto> getTransactionsByStatus(TransactionStatus status) {
        return transactionRepository.findByStatus(status).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TransactionDto> getTransactionsByType(TransactionType transactionType) {
        return transactionRepository.findByTransactionType(transactionType).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TransactionDto> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private Account getActiveAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account with id " + accountId + " not found"));
        
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Account is not active");
        }
        
        return account;
    }

    private String generateReferenceNumber() {
        return "TXN" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }

    private TransactionDto mapToDto(Transaction transaction) {
        TransactionDto dto = new TransactionDto();
        dto.setId(transaction.getId());
        dto.setTransactionType(transaction.getTransactionType());
        dto.setAmount(transaction.getAmount());
        dto.setDescription(transaction.getDescription());
        dto.setReferenceNumber(transaction.getReferenceNumber());
        dto.setStatus(transaction.getStatus());
        dto.setAccountId(transaction.getAccount().getId());
        dto.setAccountNumber(transaction.getAccount().getAccountNumber());
        dto.setBalanceAfter(transaction.getBalanceAfter());
        
        if (transaction.getDestinationAccount() != null) {
            dto.setDestinationAccountId(transaction.getDestinationAccount().getId());
            dto.setDestinationAccountNumber(transaction.getDestinationAccount().getAccountNumber());
        }
        
        return dto;
    }
}