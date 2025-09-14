package info.skantuz.personal_bank.usecase;

import info.skantuz.personal_bank.model.Account;
import info.skantuz.personal_bank.model.Customer;
import info.skantuz.personal_bank.model.error.ApiException;
import info.skantuz.personal_bank.model.value_object.AccountState;
import info.skantuz.personal_bank.model.value_object.AccountType;
import info.skantuz.personal_bank.port.out.AccountRead;
import info.skantuz.personal_bank.port.out.AccountWrite;
import info.skantuz.personal_bank.port.out.AuthValidation;
import info.skantuz.personal_bank.port.out.CustomerRead;
import info.skantuz.personal_bank.port.out.GeneralLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccountUseCaseTest {

  private AccountRead accountRead;
  private AccountWrite accountWrite;
  private CustomerRead customerRead;
  private GeneralLog generalLog;
  private AuthValidation authValidation;
  private AccountUseCase useCase;

  @BeforeEach
  void setUp() {
    accountRead = mock(AccountRead.class);
    accountWrite = mock(AccountWrite.class);
    customerRead = mock(CustomerRead.class);
    generalLog = mock(GeneralLog.class);
    authValidation = mock(AuthValidation.class);
    useCase = new AccountUseCase(accountRead, accountWrite, customerRead, generalLog, authValidation);
  }

  @Test
  void createAccountSavingsSuccess() {
    String authToken = "token";
    String user = "user";
    String identificationType = "CC";
    String identificationNumber = "123";
    String customerId = "custId";
    Customer customer = new Customer(customerId, identificationType, identificationNumber,
        null, null, null, null, null, null);
    var accountBuilder = Account.builder().accountType(AccountType.SAVINGS)
        .balance(BigDecimal.ZERO).exemptGMF(false).status(AccountState.ACTIVE);
    Account account = accountBuilder.build("5300000000", customerId);

    when(authValidation.validateToken(authToken)).thenReturn(Mono.just(user));
    when(customerRead.getCustomerByDocumentTypeAndNumber(identificationType, identificationNumber))
        .thenReturn(Mono.just(customer));
    when(accountRead.getAccountData(anyString())).thenReturn(Mono.empty());
    when(accountWrite.createAccountData(any())).thenReturn(Mono.just(account));

    StepVerifier.create(useCase.createAccount(
            identificationType, identificationNumber, accountBuilder, authToken))
        .expectNext(account)
        .verifyComplete();

  }

  @Test
  void createAccount_customerNotFound() {
    String authToken = "token";
    String user = "user";
    String identificationType = "CC";
    String identificationNumber = "123";
    Account.AccountBuilder builder = mock(Account.AccountBuilder.class);

    when(authValidation.validateToken(authToken)).thenReturn(Mono.just(user));
    when(customerRead.getCustomerByDocumentTypeAndNumber(identificationType, identificationNumber))
        .thenReturn(Mono.empty());

    StepVerifier.create(useCase.createAccount(identificationType, identificationNumber, builder, authToken))
        .verifyErrorSatisfies(error -> {
          assertInstanceOf(ApiException.class, error);
          assertEquals("The specified customer does not exist.", ((ApiException) error).getDescription());
        });
  }

  @Test
  void getAccountById_success() {
    String authToken = "token";
    String user = "user";
    String accountId = "accId";
    Account account = mock(Account.class);

    when(authValidation.validateToken(authToken)).thenReturn(Mono.just(user));
    when(accountRead.getAccountData(accountId)).thenReturn(Mono.just(account));

    StepVerifier.create(useCase.getAccountById(accountId, authToken))
        .expectNext(account)
        .verifyComplete();
  }

  @Test
  void updateAccount_success() {
    String authToken = "token";
    String user = "user";
    String accountId = "accId";
    Account account = mock(Account.class);

    when(authValidation.validateToken(authToken)).thenReturn(Mono.just(user));
    when(accountRead.getAccountData(accountId)).thenReturn(Mono.just(account));
    when(accountWrite.updateAccountData(accountId, account)).thenReturn(Mono.just(account));

    StepVerifier.create(useCase.updateAccount(accountId, account, authToken))
        .expectNext(account)
        .verifyComplete();

    verify(generalLog).logInfo(contains("updated account"));
  }

  @Test
  void updateAccount_notFound() {
    String authToken = "token";
    String user = "user";
    String accountId = "accId";
    Account account = mock(Account.class);

    when(authValidation.validateToken(authToken)).thenReturn(Mono.just(user));
    when(accountRead.getAccountData(accountId)).thenReturn(Mono.empty());

    StepVerifier.create(useCase.updateAccount(accountId, account, authToken))
        .verifyErrorSatisfies(error ->{
          assertInstanceOf(ApiException.class, error);
          assertEquals("The specified account does not exist.", ((ApiException) error).getDescription());
        });
  }

  @Test
  void deleteAccount_success() {
    String authToken = "token";
    String user = "user";
    String accountId = "accId";
    Account account = mock(Account.class);

    when(authValidation.validateToken(authToken)).thenReturn(Mono.just(user));
    when(accountRead.getAccountData(accountId)).thenReturn(Mono.just(account));
    when(accountWrite.deleteAccountData(accountId)).thenReturn(Mono.empty());

    StepVerifier.create(useCase.deleteAccount(accountId, authToken))
        .expectNext(accountId)
        .verifyComplete();

    verify(generalLog).logInfo(contains("deleted account"));
  }

  @Test
  void deleteAccount_notFound() {
    String authToken = "token";
    String user = "user";
    String accountId = "accId";

    when(authValidation.validateToken(authToken)).thenReturn(Mono.just(user));
    when(accountRead.getAccountData(accountId)).thenReturn(Mono.empty());

    StepVerifier.create(useCase.deleteAccount(accountId, authToken))
        .verifyErrorSatisfies(error ->{
          assertInstanceOf(ApiException.class, error);
          assertEquals("The specified account does not exist.", ((ApiException) error).getDescription());
        });
  }

  @Test
  void getAllAccounts_success() {
    String authToken = "token";
    String user = "user";
    Account account1 = mock(Account.class);
    Account account2 = mock(Account.class);

    when(authValidation.validateToken(authToken)).thenReturn(Mono.just(user));
    when(accountRead.getAllAccountsData()).thenReturn(Flux.just(account1, account2));

    StepVerifier.create(useCase.getAllAccounts(authToken))
        .expectNext(account1)
        .expectNext(account2)
        .verifyComplete();
  }
}