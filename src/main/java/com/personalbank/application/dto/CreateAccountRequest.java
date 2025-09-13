package com.personalbank.application.dto;

import com.personalbank.domain.model.AccountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CreateAccountRequest {
    
    @NotNull(message = "Account type is required")
    private AccountType accountType;
    
    @NotNull(message = "Client ID is required")
    private Long clientId;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "Initial deposit must be non-negative")
    private BigDecimal initialDeposit = BigDecimal.ZERO;
    
    // For SavingsAccount
    private BigDecimal interestRate;
    private BigDecimal minimumBalance;
    
    // For CheckingAccount
    private BigDecimal overdraftLimit;
    private BigDecimal monthlyFee;
    
    // Constructors
    public CreateAccountRequest() {}
    
    public CreateAccountRequest(AccountType accountType, Long clientId, BigDecimal initialDeposit) {
        this.accountType = accountType;
        this.clientId = clientId;
        this.initialDeposit = initialDeposit;
    }
    
    // Getters and Setters
    public AccountType getAccountType() {
        return accountType;
    }
    
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
    
    public Long getClientId() {
        return clientId;
    }
    
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
    
    public BigDecimal getInitialDeposit() {
        return initialDeposit;
    }
    
    public void setInitialDeposit(BigDecimal initialDeposit) {
        this.initialDeposit = initialDeposit;
    }
    
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    
    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }
    
    public void setMinimumBalance(BigDecimal minimumBalance) {
        this.minimumBalance = minimumBalance;
    }
    
    public BigDecimal getOverdraftLimit() {
        return overdraftLimit;
    }
    
    public void setOverdraftLimit(BigDecimal overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }
    
    public BigDecimal getMonthlyFee() {
        return monthlyFee;
    }
    
    public void setMonthlyFee(BigDecimal monthlyFee) {
        this.monthlyFee = monthlyFee;
    }
}