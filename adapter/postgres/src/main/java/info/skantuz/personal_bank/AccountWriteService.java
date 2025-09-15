package info.skantuz.personal_bank;

import info.skantuz.personal_bank.dto.AccountDto;
import info.skantuz.personal_bank.model.Account;
import info.skantuz.personal_bank.port.out.AccountWrite;
import info.skantuz.personal_bank.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AccountWriteService implements AccountWrite {
  private final AccountRepository repository;

  @Override
  public Mono<Account> createAccountData(Account accountData) {
    return repository.save(AccountDto.of(accountData))
        .map(AccountDto::toAccountModel);
  }

  @Override
  public Mono<Void> deleteAccountData(String accountId) {
    return repository.deleteById(accountId);
  }

  @Override
  public Mono<Account> updateAccountData(String accountId, Account accountData) {
    return repository.findById(accountId)
        .flatMap(accountDto ->
            repository.save(updateAccountDto(accountDto, accountData)))
        .map(AccountDto::toAccountModel);
  }

  private AccountDto updateAccountDto(AccountDto existingAccount, Account accountData) {
    existingAccount.setAccountType(accountData.accountType());
    existingAccount.setAccountNumber(accountData.accountNumber());
    existingAccount.setStatus(accountData.status());
    existingAccount.setBalance(accountData.balance());
    existingAccount.setExemptGMF(accountData.exemptGMF());
    existingAccount.setUpdatedAt(accountData.updatedAt());
    existingAccount.setCustomerId(accountData.customerId());
    return existingAccount;
  }
}
