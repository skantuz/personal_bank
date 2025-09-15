package info.skantuz.personal_bank.usecase;

import info.skantuz.personal_bank.model.Account;
import info.skantuz.personal_bank.model.Customer;
import info.skantuz.personal_bank.model.Transaction;
import info.skantuz.personal_bank.model.TransactionInput;
import info.skantuz.personal_bank.model.error.ApiException;
import info.skantuz.personal_bank.model.error.ErrorList;
import info.skantuz.personal_bank.model.value_object.AccountState;
import info.skantuz.personal_bank.model.value_object.AccountType;
import info.skantuz.personal_bank.model.value_object.TransactionType;
import info.skantuz.personal_bank.port.out.AccountRead;
import info.skantuz.personal_bank.port.out.AccountWrite;
import info.skantuz.personal_bank.port.out.AuthValidation;
import info.skantuz.personal_bank.port.out.CustomerRead;
import info.skantuz.personal_bank.port.out.GeneralLog;
import info.skantuz.personal_bank.port.out.TransactionsLog;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static info.skantuz.personal_bank.model.value_object.AccountType.CHECKING;
import static info.skantuz.personal_bank.model.value_object.AccountType.SAVINGS;
import static info.skantuz.personal_bank.model.value_object.TransactionType.DEPOSIT;
import static info.skantuz.personal_bank.model.value_object.TransactionType.TRANSFER;
import static info.skantuz.personal_bank.model.value_object.TransactionType.WITHDRAWAL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TransactionUseCaseTest {

  private CustomerRead customerRead = mock(CustomerRead.class);
  private AccountRead accountRead = mock(AccountRead.class);
  private AccountWrite accountWrite = mock(AccountWrite.class);
  private AuthValidation authValidation = mock(AuthValidation.class);
  private GeneralLog generalLog = mock(GeneralLog.class);
  private TransactionsLog transactionsLog = mock(TransactionsLog.class);

  private TransactionUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new TransactionUseCase(
        customerRead, accountRead, accountWrite,
        authValidation, generalLog, transactionsLog
    );
  }

  @Test
  void testDepositSuccess() {
    TransactionInput input = TransactionInput.builder().accountNumberDeposit("3312345678")
        .accountTypeDeposit(SAVINGS).type(DEPOSIT).amount(BigDecimal.TEN).build();

    Customer customer = Customer.of("C1", "ID", "123", "John", "Doe",
        "john.doe@sas.com", LocalDate.of(1990, 01, 01));

    Account account = Account.of( SAVINGS, "3312345678", AccountState.ACTIVE,
        BigDecimal.valueOf(1000L), false, customer.id());

    when(authValidation.validateToken(anyString())).thenReturn(Mono.just("user"));
    when(customerRead.getCustomerByDocumentTypeAndNumber(any(), any())).thenReturn(Mono.just(customer));
    when(accountRead.getAccountByAccountNumberAndType(any(), any())).thenReturn(Mono.just(account));
    when(accountWrite.updateAccountData(any(), any())).thenReturn(Mono.just(account));

    StepVerifier.create(useCase.transaction(input, "token"))
        .expectNext("Depósito realizado con éxito nuevo saldo: " + account.balance())
        .verifyComplete();
  }

  @Test
  void testWithdrawalInsufficientFunds() {
    TransactionInput input = TransactionInput.builder().amount(BigDecimal.valueOf(1000L))
        .identificationTypeWithdraw("ID").identificationNumberWithdraw("123")
        .accountNumberWithdraw("ACC1").accountTypeWithdraw(SAVINGS).type(WITHDRAWAL).build();

    Customer customer = Customer.builder().id("C1").build();
    Account account = Account.builder().accountType(SAVINGS).status(AccountState.ACTIVE)
        .balance(BigDecimal.valueOf(500L)).build("ACC1", customer.id());

    when(authValidation.validateToken(anyString())).thenReturn(Mono.just("user"));
    when(customerRead.getCustomerByDocumentTypeAndNumber(any(), any())).thenReturn(Mono.just(customer));
    when(accountRead.getAccountByAccountNumberAndType(any(), any())).thenReturn(Mono.just(account));

    StepVerifier.create(useCase.transaction(input, "token"))
        .verifyErrorSatisfies(throwable ->{
          Assertions.assertInstanceOf(ApiException.class, throwable);
          Assertions.assertEquals("The account does not have enough balance.", ((ApiException) throwable).getDescription());
        });
  }

  @Test
  void testTransferSuccess() {
    TransactionInput input = TransactionInput.of(TRANSFER, SAVINGS, CHECKING,
        "ACC1", "ACC2", "ID", "ID2", "123", "456", BigDecimal.TEN);

    Customer customerW = Customer.builder().id("C1").build();
    Account accountW = Account.of(SAVINGS, "ACC1", AccountState.ACTIVE,
        BigDecimal.valueOf(100L), false, customerW.id());
    Customer customerD = Customer.builder().id("C2").build();
    Account accountD = Account.of(CHECKING, "ACC2", AccountState.ACTIVE,
        BigDecimal.valueOf(50L), false, customerD.id());

    when(authValidation.validateToken(anyString())).thenReturn(Mono.just("user"));
    when(customerRead.getCustomerByDocumentTypeAndNumber("ID", "123")).thenReturn(Mono.just(customerW));
    when(accountRead.getAccountByAccountNumberAndType("ACC1", SAVINGS)).thenReturn(Mono.just(accountW));
    when(customerRead.getCustomerByDocumentTypeAndNumber("ID2", "456")).thenReturn(Mono.just(customerD));
    when(accountRead.getAccountByAccountNumberAndType("ACC2", CHECKING)).thenReturn(Mono.just(accountD));
    when(accountWrite.updateAccountData(any(), any())).thenReturn(Mono.just(accountW))
        .thenReturn(Mono.just(accountD));

    StepVerifier.create(useCase.transaction(input, "token"))
        .expectNext("Transferencia realizada con éxito nuevo saldo: " + accountD.balance())
        .verifyComplete();
  }
}