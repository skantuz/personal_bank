package info.skantuz.personal_bank.usecase;


import info.skantuz.personal_bank.model.Account;
import info.skantuz.personal_bank.model.error.ErrorList;
import info.skantuz.personal_bank.model.value_object.AccountType;
import info.skantuz.personal_bank.port.in.AccountIn;
import info.skantuz.personal_bank.port.out.AccountRead;
import info.skantuz.personal_bank.port.out.AccountWrite;
import info.skantuz.personal_bank.port.out.AuthValidation;
import info.skantuz.personal_bank.port.out.CustomerRead;
import info.skantuz.personal_bank.port.out.GeneralLog;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.SecureRandom;

@RequiredArgsConstructor
public final class AccountUseCase implements AccountIn {

  private final AccountRead accountRead;

  private final AccountWrite accountWrite;

  private final CustomerRead customerRead;

  private final GeneralLog generalLog;

  private final AuthValidation authValidation;

  @Override
  public Mono<Account> createAccount(String identificationType, String identificationNumber,
                                     Account.AccountBuilder account, String authToken) {
    return authValidation.validateToken(authToken)
        .flatMap(user -> customerRead.getCustomerByDocumentTypeAndNumber(identificationType, identificationNumber)
        .switchIfEmpty(Mono.error(ErrorList.CUSTOMER_NOT_FOUND
            .toApiException("Customer with document type %s and number %s not found"
            .formatted(identificationType, identificationNumber))))
        .flatMap(customer -> generateAccountNumber(account.getAccountType())
            .map(accountNumber -> account.build(accountNumber, customer.id())))
        .flatMap(accountWrite::createAccountData)
            .doOnNext(accountCreated ->
                generalLog.logInfo("User: %s created a new account with id: %s"
                .formatted(user, accountCreated.id()))));
  }

  @Override
  public Mono<Account> getAccountById(String accountId, String authToken) {
    return authValidation.validateToken(authToken)
        .flatMap(user -> accountRead.getAccountData(accountId));
  }

  @Override
  public Mono<Account> updateAccount(String accountId, Account account, String authToken) {
    return authValidation.validateToken(authToken)
        .flatMap(user -> accountRead.getAccountData(accountId)
            .switchIfEmpty(Mono.error(ErrorList.ACCOUNT_NOT_FOUND
                .toApiException("Account with id %s not found".formatted(accountId))))
            .flatMap(accountExisting -> accountWrite.updateAccountData(accountId, account)
                .doOnNext(accountUpdated ->
                    generalLog.logInfo("User: %s updated account with id: %s"
                        .formatted(user, accountId)))));
  }

  @Override
  public Mono<String> deleteAccount(String accountId, String authToken) {
    return authValidation.validateToken(authToken)
        .flatMap(user -> accountRead.getAccountData(accountId)
            .switchIfEmpty(Mono.error(ErrorList.ACCOUNT_NOT_FOUND
                .toApiException("Account with id %s not found".formatted(accountId))))
            .flatMap(accountExisting -> accountWrite.deleteAccountData(accountId)
                .then(Mono.just(accountId))
                .doOnNext(deletedId ->
                    generalLog.logInfo("User: %s deleted account with id: %s"
                        .formatted(user, deletedId)))));
  }

  @Override
  public Flux<Account> getAllAccounts(String authToken) {
    return authValidation.validateToken(authToken)
        .flatMapMany(user -> accountRead.getAllAccountsData());
  }

  private Mono<String> generateAccountNumber(AccountType accountType) {
    var accountNumberBuilder = switch (accountType) {
      case SAVINGS -> new StringBuilder("53");
      case CHECKING -> new StringBuilder("33");
    };

    final SecureRandom random = new SecureRandom();
    for (int i = 0; i < 8; i++) {
      accountNumberBuilder.append(random.nextInt(10));
    }

    var accountNumber = accountNumberBuilder.toString();
    return  accountRead.getAccountData(accountNumber)
        .hasElement()
        .flatMap(exists -> exists
            ? generateAccountNumber(accountType) : Mono.just(accountNumber));

  }
}