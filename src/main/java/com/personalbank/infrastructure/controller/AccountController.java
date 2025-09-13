package com.personalbank.infrastructure.controller;

import com.personalbank.application.dto.AccountDto;
import com.personalbank.application.service.AccountService;
import com.personalbank.domain.model.AccountStatus;
import com.personalbank.domain.model.AccountType;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
@CrossOrigin(origins = "*")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@Valid @RequestBody AccountDto accountDto) {
        try {
            AccountDto createdAccount = accountService.createAccount(accountDto);
            return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable Long id) {
        Optional<AccountDto> account = accountService.getAccountById(id);
        return account.map(a -> new ResponseEntity<>(a, HttpStatus.OK))
                     .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<AccountDto> getAccountByAccountNumber(@PathVariable String accountNumber) {
        Optional<AccountDto> account = accountService.getAccountByAccountNumber(accountNumber);
        return account.map(a -> new ResponseEntity<>(a, HttpStatus.OK))
                     .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<AccountDto>> getAccountsByClientId(@PathVariable Long clientId) {
        List<AccountDto> accounts = accountService.getAccountsByClientId(clientId);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<AccountDto>> getAccountsByStatus(@PathVariable AccountStatus status) {
        List<AccountDto> accounts = accountService.getAccountsByStatus(status);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @GetMapping("/type/{accountType}")
    public ResponseEntity<List<AccountDto>> getAccountsByType(@PathVariable AccountType accountType) {
        List<AccountDto> accounts = accountService.getAccountsByType(accountType);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        List<AccountDto> accounts = accountService.getAllAccounts();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<AccountDto> updateAccountStatus(@PathVariable Long id, @RequestParam AccountStatus status) {
        try {
            AccountDto updatedAccount = accountService.updateAccountStatus(id, status);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        try {
            accountService.deleteAccount(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<BigDecimal> getAccountBalance(@PathVariable Long id) {
        try {
            BigDecimal balance = accountService.getAccountBalance(id);
            return new ResponseEntity<>(balance, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}