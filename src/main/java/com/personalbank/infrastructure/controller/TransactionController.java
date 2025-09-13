package com.personalbank.infrastructure.controller;

import com.personalbank.application.dto.TransactionDto;
import com.personalbank.application.service.TransactionService;
import com.personalbank.domain.model.TransactionStatus;
import com.personalbank.domain.model.TransactionType;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionDto> deposit(@RequestParam Long accountId, 
                                                 @RequestParam BigDecimal amount,
                                                 @RequestParam(required = false) String description) {
        try {
            TransactionDto transaction = transactionService.deposit(accountId, amount, description);
            return new ResponseEntity<>(transaction, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionDto> withdraw(@RequestParam Long accountId, 
                                                  @RequestParam BigDecimal amount,
                                                  @RequestParam(required = false) String description) {
        try {
            TransactionDto transaction = transactionService.withdraw(accountId, amount, description);
            return new ResponseEntity<>(transaction, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionDto> transfer(@RequestParam Long fromAccountId,
                                                  @RequestParam Long toAccountId,
                                                  @RequestParam BigDecimal amount,
                                                  @RequestParam(required = false) String description) {
        try {
            TransactionDto transaction = transactionService.transfer(fromAccountId, toAccountId, amount, description);
            return new ResponseEntity<>(transaction, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto> getTransactionById(@PathVariable Long id) {
        Optional<TransactionDto> transaction = transactionService.getTransactionById(id);
        return transaction.map(t -> new ResponseEntity<>(t, HttpStatus.OK))
                         .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/reference/{referenceNumber}")
    public ResponseEntity<TransactionDto> getTransactionByReferenceNumber(@PathVariable String referenceNumber) {
        Optional<TransactionDto> transaction = transactionService.getTransactionByReferenceNumber(referenceNumber);
        return transaction.map(t -> new ResponseEntity<>(t, HttpStatus.OK))
                         .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionDto>> getTransactionsByAccountId(@PathVariable Long accountId) {
        List<TransactionDto> transactions = transactionService.getTransactionsByAccountId(accountId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/account/{accountId}/daterange")
    public ResponseEntity<List<TransactionDto>> getTransactionsByAccountIdAndDateRange(
            @PathVariable Long accountId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<TransactionDto> transactions = transactionService.getTransactionsByAccountIdAndDateRange(accountId, startDate, endDate);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TransactionDto>> getTransactionsByStatus(@PathVariable TransactionStatus status) {
        List<TransactionDto> transactions = transactionService.getTransactionsByStatus(status);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/type/{transactionType}")
    public ResponseEntity<List<TransactionDto>> getTransactionsByType(@PathVariable TransactionType transactionType) {
        List<TransactionDto> transactions = transactionService.getTransactionsByType(transactionType);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TransactionDto>> getAllTransactions() {
        List<TransactionDto> transactions = transactionService.getAllTransactions();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}