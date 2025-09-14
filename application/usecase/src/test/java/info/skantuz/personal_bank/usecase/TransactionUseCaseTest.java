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

    when(account.deposit(any())).thenReturn(account);

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
    TransactionInput input = mock(TransactionInput.class);
    when(input.type()).thenReturn(WITHDRAWAL);
    when(input.identificationTypeWithdraw()).thenReturn("ID");
    when(input.identificationNumberWithdraw()).thenReturn("123");
    when(input.accountNumberWithdraw()).thenReturn("ACC1");
    when(input.accountTypeWithdraw()).thenReturn(SAVINGS);
    when(input.amount()).thenReturn(BigDecimal.TEN);

    Customer customer = mock(Customer.class);
    when(customer.id()).thenReturn("C1");
    Account account = mock(Account.class);
    when(account.customerId()).thenReturn("C1");
    when(account.canWithdraw(any())).thenReturn(false);

    when(authValidation.validateToken(anyString())).thenReturn(Mono.just("user"));
    when(customerRead.getCustomerByDocumentTypeAndNumber(any(), any())).thenReturn(Mono.just(customer));
    when(accountRead.getAccountByAccountNumberAndType(any(), any())).thenReturn(Mono.just(account));

    StepVerifier.create(useCase.transaction(input, "token"))
        .verifyErrorSatisfies(throwable ->{
          Assertions.assertInstanceOf(ApiException.class, throwable);
          Assertions.assertEquals("Insufficient funds for WITHDRAWAL", ((ApiException) throwable).getDescription());
        });
  }

  @Test
  void testTransferSuccess() {
    TransactionInput input = mock(TransactionInput.class);
    when(input.type()).thenReturn(TransactionType.TRANSFER);

    // Withdrawal mocks
    when(input.identificationTypeWithdraw()).thenReturn("ID");
    when(input.identificationNumberWithdraw()).thenReturn("123");
    when(input.accountNumberWithdraw()).thenReturn("ACC1");
    when(input.accountTypeWithdraw()).thenReturn(SAVINGS);
    when(input.amount()).thenReturn(BigDecimal.TEN);

    // Deposit mocks
    when(input.identificationTypeDeposit()).thenReturn("ID2");
    when(input.identificationNumberDeposit()).thenReturn("456");
    when(input.accountNumberDeposit()).thenReturn("ACC2");
    when(input.accountTypeDeposit()).thenReturn(CHECKING);

    Customer customerW = mock(Customer.class);
    when(customerW.id()).thenReturn("C1");
    Account accountW = mock(Account.class);
    when(accountW.customerId()).thenReturn("C1");
    when(accountW.canWithdraw(any())).thenReturn(true);
    when(accountW.withdraw(any())).thenReturn(accountW);

    Customer customerD = mock(Customer.class);
    when(customerD.id()).thenReturn("C2");
    Account accountD = mock(Account.class);
    when(accountD.customerId()).thenReturn("C2");
    when(accountD.deposit(any())).thenReturn(accountD);

    when(authValidation.validateToken(anyString())).thenReturn(Mono.just("user"));
    when(customerRead.getCustomerByDocumentTypeAndNumber("ID", "123")).thenReturn(Mono.just(customerW));
    when(accountRead.getAccountByAccountNumberAndType("ACC1", SAVINGS)).thenReturn(Mono.just(accountW));
    when(customerRead.getCustomerByDocumentTypeAndNumber("ID2", "456")).thenReturn(Mono.just(customerD));
    when(accountRead.getAccountByAccountNumberAndType("ACC2", CHECKING)).thenReturn(Mono.just(accountD));
    when(accountWrite.updateAccountData(any(), any())).thenReturn(Mono.just(accountW))
        .thenReturn(Mono.just(accountD));

    Transaction transactionW = mock(Transaction.class);
    when(transactionW.withdrawAccount()).thenReturn(accountW);
    when(transactionW.amount()).thenReturn(BigDecimal.TEN);
    when(transactionW.type()).thenReturn(WITHDRAWAL);
    when(transactionW.id()).thenReturn("T1");
    when(Transaction.ofWithdrawal(any(), any(), any())).thenReturn(transactionW);

    Transaction transactionD = mock(Transaction.class);
    when(transactionD.depositAccount()).thenReturn(accountD);
    when(transactionD.amount()).thenReturn(BigDecimal.TEN);
    when(transactionD.type()).thenReturn(DEPOSIT);
    when(transactionD.id()).thenReturn("T2");
    when(Transaction.ofDeposit(any(), any(), any())).thenReturn(transactionD);

    StepVerifier.create(useCase.transaction(input, "token"))
        .expectNext("Transferencia realizada con éxito nuevo saldo: " + accountD.balance())
        .verifyComplete();
  }
}