package info.skantuz.personal_bank.repository;

import info.skantuz.personal_bank.dto.AccountDto;
import info.skantuz.personal_bank.model.value_object.AccountType;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface AccountRepository extends R2dbcRepository<AccountDto, String> {
  Mono<AccountDto> findByAccountNumberAndAccountType(String accountNumber, AccountType accountType);
}
