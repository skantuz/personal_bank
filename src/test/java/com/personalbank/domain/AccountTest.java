package com.personalbank.domain;

import com.personalbank.domain.model.Account;
import com.personalbank.domain.model.AccountType;
import com.personalbank.domain.model.AccountStatus;
import com.personalbank.domain.model.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private Client testClient;

    @BeforeEach
    void setUp() {
        testClient = new Client("John", "Doe", "john.doe@example.com", "+1234567890", "123 Main St");
        testClient.setId(1L);
    }

    @Test
    void testAccountCreation() {
        Account account = new Account("ACC123456789", AccountType.CHECKING, testClient);
        
        assertEquals("ACC123456789", account.getAccountNumber());
        assertEquals(AccountType.CHECKING, account.getAccountType());
        assertEquals(BigDecimal.ZERO, account.getBalance());
        assertEquals(AccountStatus.ACTIVE, account.getStatus());
        assertEquals(testClient, account.getClient());
        assertNotNull(account.getCreatedAt());
        assertNull(account.getUpdatedAt());
    }

    @Test
    void testAccountCreationWithInitialBalance() {
        BigDecimal initialBalance = new BigDecimal("1000.00");
        Account account = new Account("ACC123456789", AccountType.SAVINGS, initialBalance, testClient);
        
        assertEquals(initialBalance, account.getBalance());
    }

    @Test
    void testDeposit() {
        Account account = new Account("ACC123456789", AccountType.CHECKING, testClient);
        BigDecimal depositAmount = new BigDecimal("500.00");
        
        account.deposit(depositAmount);
        
        assertEquals(depositAmount, account.getBalance());
    }

    @Test
    void testDepositInvalidAmount() {
        Account account = new Account("ACC123456789", AccountType.CHECKING, testClient);
        
        assertThrows(IllegalArgumentException.class, () -> account.deposit(BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class, () -> account.deposit(new BigDecimal("-100")));
        assertThrows(IllegalArgumentException.class, () -> account.deposit(null));
    }

    @Test
    void testWithdraw() {
        Account account = new Account("ACC123456789", AccountType.CHECKING, new BigDecimal("1000.00"), testClient);
        BigDecimal withdrawAmount = new BigDecimal("300.00");
        
        account.withdraw(withdrawAmount);
        
        assertEquals(new BigDecimal("700.00"), account.getBalance());
    }

    @Test
    void testWithdrawInsufficientFunds() {
        Account account = new Account("ACC123456789", AccountType.CHECKING, new BigDecimal("100.00"), testClient);
        BigDecimal withdrawAmount = new BigDecimal("200.00");
        
        assertThrows(IllegalStateException.class, () -> account.withdraw(withdrawAmount));
    }

    @Test
    void testWithdrawInvalidAmount() {
        Account account = new Account("ACC123456789", AccountType.CHECKING, new BigDecimal("1000.00"), testClient);
        
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(new BigDecimal("-100")));
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(null));
    }

    @Test
    void testDepositToInactiveAccount() {
        Account account = new Account("ACC123456789", AccountType.CHECKING, testClient);
        account.setStatus(AccountStatus.INACTIVE);
        
        assertThrows(IllegalStateException.class, () -> account.deposit(new BigDecimal("100.00")));
    }

    @Test
    void testWithdrawFromInactiveAccount() {
        Account account = new Account("ACC123456789", AccountType.CHECKING, new BigDecimal("1000.00"), testClient);
        account.setStatus(AccountStatus.INACTIVE);
        
        assertThrows(IllegalStateException.class, () -> account.withdraw(new BigDecimal("100.00")));
    }

    @Test
    void testHasSufficientFunds() {
        Account account = new Account("ACC123456789", AccountType.CHECKING, new BigDecimal("1000.00"), testClient);
        
        assertTrue(account.hasSufficientFunds(new BigDecimal("500.00")));
        assertTrue(account.hasSufficientFunds(new BigDecimal("1000.00")));
        assertFalse(account.hasSufficientFunds(new BigDecimal("1500.00")));
    }
}