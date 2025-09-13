package com.personalbank.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("SAVINGS")
public class SavingsAccount extends Account {
    
    @NotNull(message = "Interest rate is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Interest rate must be non-negative")
    @Column(name = "interest_rate", precision = 5, scale = 4)
    private BigDecimal interestRate;
    
    @NotNull(message = "Minimum balance is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Minimum balance must be non-negative")
    @Column(name = "minimum_balance", precision = 19, scale = 2)
    private BigDecimal minimumBalance;
    
    // Constructors
    public SavingsAccount() {
        super();
        this.interestRate = new BigDecimal("0.0250"); // 2.5% default interest rate
        this.minimumBalance = new BigDecimal("100.00"); // $100 minimum balance
    }
    
    public SavingsAccount(String accountNumber, Client client) {
        super(accountNumber, client);
        this.interestRate = new BigDecimal("0.0250");
        this.minimumBalance = new BigDecimal("100.00");
    }
    
    public SavingsAccount(String accountNumber, Client client, BigDecimal interestRate, BigDecimal minimumBalance) {
        super(accountNumber, client);
        this.interestRate = interestRate;
        this.minimumBalance = minimumBalance;
    }
    
    @Override
    public AccountType getAccountType() {
        return AccountType.SAVINGS;
    }
    
    @Override
    public boolean canWithdraw(BigDecimal amount) {
        BigDecimal balanceAfterWithdrawal = getBalance().subtract(amount);
        return balanceAfterWithdrawal.compareTo(minimumBalance) >= 0;
    }
    
    public BigDecimal calculateInterest() {
        return getBalance().multiply(interestRate);
    }
    
    public void applyInterest() {
        if (isActive()) {
            BigDecimal interest = calculateInterest();
            deposit(interest);
        }
    }
    
    // Getters and Setters
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
    
    @Override
    public String toString() {
        return "SavingsAccount{" +
                "id=" + getId() +
                ", accountNumber='" + getAccountNumber() + '\'' +
                ", balance=" + getBalance() +
                ", interestRate=" + interestRate +
                ", minimumBalance=" + minimumBalance +
                ", status=" + getStatus() +
                '}';
    }
}