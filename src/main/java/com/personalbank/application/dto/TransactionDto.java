package com.personalbank.application.dto;

import com.personalbank.domain.model.TransactionStatus;
import com.personalbank.domain.model.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class TransactionDto {
    private Long id;

    @NotNull(message = "Transaction type is required")
    private TransactionType transactionType;

    @DecimalMin(value = "0.01", message = "Amount must be positive")
    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    private String description;

    private String referenceNumber;

    private TransactionStatus status;

    @NotNull(message = "Account ID is required")
    private Long accountId;

    private String accountNumber;

    private Long destinationAccountId;

    private String destinationAccountNumber;

    private BigDecimal balanceAfter;

    public TransactionDto() {}

    public TransactionDto(TransactionType transactionType, BigDecimal amount, Long accountId) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.accountId = accountId;
        this.status = TransactionStatus.PENDING;
    }

    public TransactionDto(TransactionType transactionType, BigDecimal amount, Long accountId, Long destinationAccountId, String description) {
        this(transactionType, amount, accountId);
        this.destinationAccountId = destinationAccountId;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getDestinationAccountId() {
        return destinationAccountId;
    }

    public void setDestinationAccountId(Long destinationAccountId) {
        this.destinationAccountId = destinationAccountId;
    }

    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }

    public void setDestinationAccountNumber(String destinationAccountNumber) {
        this.destinationAccountNumber = destinationAccountNumber;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
    }
}