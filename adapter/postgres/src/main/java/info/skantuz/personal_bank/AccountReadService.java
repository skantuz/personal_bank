package info.skantuz.personal_bank;

import info.skantuz.personal_bank.dto.AccountDto;
import info.skantuz.personal_bank.model.Account;
import info.skantuz.personal_bank.model.value_object.AccountType;
import info.skantuz.personal_bank.port.out.AccountRead;
import info.skantuz.personal_bank.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AccountReadService implements AccountRead {

  private final AccountRepository repository;

  @Override
  public Mono<Account> getAccountData(String accountId) {
    return repository.findById(accountId).map(AccountDto::toAccountModel);
  }

  @Override
  public Mono<Account> getAccountByAccountNumberAndType(String accountNumber, AccountType accountType) {
    return repository.findByAccountNumberAndAccountType(accountNumber, accountType)
        .map(AccountDto::toAccountModel);
  }

  @Override
  public Flux<Account> getAllAccountsData() {
    return repository.findAll().map(AccountDto::toAccountModel);
  }
}
