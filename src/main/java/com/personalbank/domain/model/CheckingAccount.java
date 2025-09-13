package com.personalbank.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("CHECKING")
public class CheckingAccount extends Account {
    
    @NotNull(message = "Overdraft limit is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Overdraft limit must be non-negative")
    @Column(name = "overdraft_limit", precision = 19, scale = 2)
    private BigDecimal overdraftLimit;
    
    @NotNull(message = "Monthly fee is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Monthly fee must be non-negative")
    @Column(name = "monthly_fee", precision = 19, scale = 2)
    private BigDecimal monthlyFee;
    
    // Constructors
    public CheckingAccount() {
        super();
        this.overdraftLimit = new BigDecimal("500.00"); // $500 default overdraft limit
        this.monthlyFee = new BigDecimal("10.00"); // $10 monthly fee
    }
    
    public CheckingAccount(String accountNumber, Client client) {
        super(accountNumber, client);
        this.overdraftLimit = new BigDecimal("500.00");
        this.monthlyFee = new BigDecimal("10.00");
    }
    
    public CheckingAccount(String accountNumber, Client client, BigDecimal overdraftLimit, BigDecimal monthlyFee) {
        super(accountNumber, client);
        this.overdraftLimit = overdraftLimit;
        this.monthlyFee = monthlyFee;
    }
    
    @Override
    public AccountType getAccountType() {
        return AccountType.CHECKING;
    }
    
    @Override
    public boolean canWithdraw(BigDecimal amount) {
        BigDecimal availableBalance = getBalance().add(overdraftLimit);
        return amount.compareTo(availableBalance) <= 0;
    }
    
    public void applyMonthlyFee() {
        if (isActive() && monthlyFee.compareTo(BigDecimal.ZERO) > 0) {
            if (canWithdraw(monthlyFee)) {
                withdraw(monthlyFee);
            }
        }
    }
    
    public BigDecimal getAvailableBalance() {
        return getBalance().add(overdraftLimit);
    }
    
    public boolean isOverdrawn() {
        return getBalance().compareTo(BigDecimal.ZERO) < 0;
    }
    
    public BigDecimal getOverdraftAmount() {
        if (isOverdrawn()) {
            return getBalance().abs();
        }
        return BigDecimal.ZERO;
    }
    
    // Getters and Setters
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
    
    @Override
    public String toString() {
        return "CheckingAccount{" +
                "id=" + getId() +
                ", accountNumber='" + getAccountNumber() + '\'' +
                ", balance=" + getBalance() +
                ", overdraftLimit=" + overdraftLimit +
                ", monthlyFee=" + monthlyFee +
                ", status=" + getStatus() +
                '}';
    }
}