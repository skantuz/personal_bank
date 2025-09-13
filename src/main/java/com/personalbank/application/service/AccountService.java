package com.personalbank.application.service;

import com.personalbank.application.dto.AccountDto;
import com.personalbank.domain.model.Account;
import com.personalbank.domain.model.AccountStatus;
import com.personalbank.domain.model.AccountType;
import com.personalbank.domain.model.Client;
import com.personalbank.domain.repository.AccountRepository;
import com.personalbank.domain.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, ClientRepository clientRepository) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
    }

    public AccountDto createAccount(AccountDto accountDto) {
        validateAccountDto(accountDto);

        Client client = clientRepository.findById(accountDto.getClientId())
                .orElseThrow(() -> new IllegalArgumentException("Client with id " + accountDto.getClientId() + " not found"));

        String accountNumber = generateAccountNumber();
        while (accountRepository.existsByAccountNumber(accountNumber)) {
            accountNumber = generateAccountNumber();
        }

        Account account = new Account(accountNumber, accountDto.getAccountType(), client);
        if (accountDto.getBalance() != null && accountDto.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            account.setBalance(accountDto.getBalance());
        }

        Account savedAccount = accountRepository.save(account);
        return mapToDto(savedAccount);
    }

    @Transactional(readOnly = true)
    public Optional<AccountDto> getAccountById(Long id) {
        return accountRepository.findById(id).map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public Optional<AccountDto> getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber).map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public List<AccountDto> getAccountsByClientId(Long clientId) {
        return accountRepository.findByClientId(clientId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AccountDto> getAccountsByStatus(AccountStatus status) {
        return accountRepository.findByStatus(status).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AccountDto> getAccountsByType(AccountType accountType) {
        return accountRepository.findByAccountType(accountType).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AccountDto> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public AccountDto updateAccountStatus(Long id, AccountStatus status) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account with id " + id + " not found"));

        account.setStatus(status);
        Account updatedAccount = accountRepository.save(account);
        return mapToDto(updatedAccount);
    }

    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account with id " + id + " not found"));

        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalStateException("Cannot delete account with non-zero balance");
        }

        accountRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public BigDecimal getAccountBalance(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account with id " + id + " not found"));
        return account.getBalance();
    }

    private String generateAccountNumber() {
        return "ACC" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }

    private void validateAccountDto(AccountDto accountDto) {
        if (accountDto == null) {
            throw new IllegalArgumentException("Account data cannot be null");
        }
        if (accountDto.getAccountType() == null) {
            throw new IllegalArgumentException("Account type is required");
        }
        if (accountDto.getClientId() == null) {
            throw new IllegalArgumentException("Client ID is required");
        }
        if (accountDto.getBalance() != null && accountDto.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
    }

    private AccountDto mapToDto(Account account) {
        AccountDto dto = new AccountDto();
        dto.setId(account.getId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setAccountType(account.getAccountType());
        dto.setBalance(account.getBalance());
        dto.setStatus(account.getStatus());
        dto.setClientId(account.getClient().getId());
        dto.setClientName(account.getClient().getFullName());
        return dto;
    }
}