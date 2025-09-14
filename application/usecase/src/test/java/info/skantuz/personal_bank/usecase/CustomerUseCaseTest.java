package info.skantuz.personal_bank.usecase;

import info.skantuz.personal_bank.model.Customer;
import info.skantuz.personal_bank.model.error.ApiException;
import info.skantuz.personal_bank.model.error.ErrorList;
import info.skantuz.personal_bank.port.out.AuthValidation;
import info.skantuz.personal_bank.port.out.CustomerRead;
import info.skantuz.personal_bank.port.out.CustomerWrite;
import info.skantuz.personal_bank.port.out.GeneralLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static info.skantuz.personal_bank.model.error.ErrorList.CUSTOMER_ALREADY_EXISTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerUseCaseTest {
  private CustomerRead customerRead;
  private CustomerWrite customerWrite;
  private AuthValidation authValidation;
  private CustomerUseCase customerUseCase;

  @BeforeEach
  void setUp() {
    customerRead = mock(CustomerRead.class);
    customerWrite = mock(CustomerWrite.class);
    authValidation = mock(AuthValidation.class);
    GeneralLog generalLog = mock(GeneralLog.class);
    customerUseCase = new CustomerUseCase(customerRead, customerWrite, authValidation, generalLog);
  }

  @Test
  void createCustomer_whenCustomerAlReadyExists_shouldReturnError() {
    Customer customer = Customer.of("id", "DNI", "123", "John", "Doe",
        "jhon.doe@sas.com", LocalDate.of(1999, 12, 31));
    when(authValidation.validateToken(anyString())).thenReturn(Mono.just("user"));
    when(customerRead.getCustomerByDocumentTypeAndNumber(anyString(), anyString()))
        .thenReturn(Mono.just(customer));

    StepVerifier.create(customerUseCase.createCustomer(customer, "token"))
        .verifyErrorSatisfies(error -> {
          assertInstanceOf(ApiException.class, error);
          assertEquals(CUSTOMER_ALREADY_EXISTS.toApiException().getCode(), ((ApiException) error).getCode());
        });
  }

  @Test
  void createCustomer_whenCustomerDoesNotExist_shouldCreateCustomer() {
    Customer customer = Customer.builder().identificationType("DNI").identificationNumber("123")
        .name("John").lastname("Doe").email("john.doe@sas.com").birthdate(LocalDate.of(1999, 12, 31))
        .id("id").build();
    Customer savedCustomer = Customer.of("id", "DNI", "123", "John", "Doe",
        "jhon.doe@sas.com", LocalDate.of(1999, 12, 31));
    when(authValidation.validateToken(anyString())).thenReturn(Mono.just("user"));
    when(customerRead.getCustomerByDocumentTypeAndNumber(anyString(), anyString()))
        .thenReturn(Mono.empty());
    when(customerWrite.createCustomerData(any(Customer.class))).thenReturn(Mono.just(savedCustomer));

    StepVerifier.create(customerUseCase.createCustomer(customer, "token"))
        .expectNext(savedCustomer)
        .verifyComplete();
  }

  @Test
  void getCustomerById_whenCustomerExists_shouldReturnCustomer() {
    Customer customer = mock(Customer.class);
    when(authValidation.validateToken(anyString())).thenReturn(Mono.just("user"));
    when(customerRead.getCustomerData(anyString())).thenReturn(Mono.just(customer));

    StepVerifier.create(customerUseCase.getCustomerById("id", "token"))
        .expectNext(customer)
        .verifyComplete();
  }

  @Test
  void getCustomerById_whenCustomerNotFound_shouldReturnError() {
    when(authValidation.validateToken(anyString())).thenReturn(Mono.just("user"));
    when(customerRead.getCustomerData(anyString())).thenReturn(Mono.empty());

    StepVerifier.create(customerUseCase.getCustomerById("id", "token"))
        .expectErrorSatisfies(error -> {
          assertInstanceOf(ApiException.class, error);
          assertEquals(ErrorList.CUSTOMER_NOT_FOUND.toApiException().getCode(), ((ApiException) error).getCode());
        })
        .verify();
  }

  @Test
  void updateCustomer_whenCustomerExists_shouldUpdateCustomer() {
    Customer customer = mock(Customer.class);
    Customer updatedCustomer = mock(Customer.class);
    when(authValidation.validateToken(anyString())).thenReturn(Mono.just("user"));
    when(customerRead.getCustomerData(anyString())).thenReturn(Mono.just(customer));
    when(customerWrite.updateCustomerData(anyString(), any(Customer.class))).thenReturn(Mono.just(updatedCustomer));

    StepVerifier.create(customerUseCase.updateCustomer("id", customer, "token"))
        .expectNext(updatedCustomer)
        .verifyComplete();
  }

  @Test
  void updateCustomer_whenCustomerNotFound_shouldReturnError() {
    Customer customer = mock(Customer.class);
    when(authValidation.validateToken(anyString())).thenReturn(Mono.just("user"));
    when(customerRead.getCustomerData(anyString())).thenReturn(Mono.empty());

    StepVerifier.create(customerUseCase.updateCustomer("id", customer, "token"))
        .expectErrorSatisfies(error -> {
          assertInstanceOf(ApiException.class, error);
          assertEquals(ErrorList.CUSTOMER_NOT_FOUND.toApiException().getCode(), ((ApiException) error).getCode());
        })
        .verify();
  }

  @Test
  void deleteCustomer_whenCustomerExists_shouldDeleteCustomer() {
    Customer customer = mock(Customer.class);
    when(authValidation.validateToken(anyString())).thenReturn(Mono.just("user"));
    when(customerRead.getCustomerData(anyString())).thenReturn(Mono.just(customer));
    when(customerWrite.deleteCustomerData(anyString())).thenReturn(Mono.just("deleted"));

    StepVerifier.create(customerUseCase.deleteCustomer("id", "token"))
        .expectNext("deleted")
        .verifyComplete();
  }

  @Test
  void deleteCustomer_whenCustomerNotFound_shouldReturnEmpty() {
    when(authValidation.validateToken(anyString())).thenReturn(Mono.just("user"));
    when(customerRead.getCustomerData(anyString())).thenReturn(Mono.empty());

    StepVerifier.create(customerUseCase.deleteCustomer("id", "token"))
        .expectComplete()
        .verify();
  }

  @Test
  void getAllCustomers_shouldReturnAllCustomers() {
    Customer customer1 = mock(Customer.class);
    Customer customer2 = mock(Customer.class);
    when(authValidation.validateToken(anyString())).thenReturn(Mono.just("user"));
    when(customerRead.getAllCustomersData()).thenReturn(Flux.just(customer1, customer2));

    StepVerifier.create(customerUseCase.getAllCustomers("token"))
        .expectNext(customer1)
        .expectNext(customer2)
        .verifyComplete();
  }
}