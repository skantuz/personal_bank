package info.skantuz.personal_bank.usecase;

import info.skantuz.personal_bank.model.Customer;
import info.skantuz.personal_bank.model.error.ErrorList;
import info.skantuz.personal_bank.port.in.CustomerIn;
import info.skantuz.personal_bank.port.out.AuthValidation;
import info.skantuz.personal_bank.port.out.CustomerRead;
import info.skantuz.personal_bank.port.out.CustomerWrite;
import info.skantuz.personal_bank.port.out.GeneralLog;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static info.skantuz.personal_bank.model.error.ErrorList.CUSTOMER_ALREADY_EXISTS;

/**
 * Implementation of the CustomerIn interface for customer-related operations.
 */
@RequiredArgsConstructor
public final class CustomerUseCase implements CustomerIn {

  private final CustomerRead customerRead;

  private final CustomerWrite customerWrite;

  private final AuthValidation authValidation;

  private final GeneralLog generalLog;

  @Override
  public Mono<Customer> createCustomer(Customer customer, String authToken) {
    return authValidation.validateToken(authToken)
        .flatMap( user -> customerRead.getCustomerByDocumentTypeAndNumber(
            customer.identificationType(), customer.identificationNumber())
            .hasElement()
            .flatMap(exist -> exist ? Mono.<Customer>error(CUSTOMER_ALREADY_EXISTS.toApiException(
                    "Customer with document type %s and number %s already exists".formatted(
                customer.identificationType(), customer.identificationNumber()))) :
        customerWrite.createCustomerData(customer)
            .doOnNext(customerSave ->
                generalLog.logInfo("User: %s created a new customer with id: %s"
                    .formatted(user, customerSave.id())))));
  }

  @Override
  public Mono<Customer> getCustomerById(String customerId, String authToken) {
    return authValidation.validateToken(authToken)
        .flatMap(user -> customerRead.getCustomerData(customerId))
        .switchIfEmpty(Mono.error(ErrorList.CUSTOMER_NOT_FOUND
            .toApiException("Customer with id %s not found".formatted(customerId))));
  }

  @Override
  public Mono<Customer> updateCustomer(String customerId, Customer customer, String authToken) {
    return authValidation.validateToken(authToken)
        .flatMap(user -> customerRead.getCustomerData(customerId)
            .flatMap(customerExisting -> customerWrite.updateCustomerData(customerId, customer)
                .doOnNext(customerUpdated ->
                    generalLog.logInfo("User: %s updated customer with id: %s"
                        .formatted(user, customerId)))))
        .switchIfEmpty(Mono.error(ErrorList.CUSTOMER_NOT_FOUND
            .toApiException("Customer with id %s not found".formatted(customerId))));

  }

  @Override
  public Mono<String> deleteCustomer(String customerId, String authToken) {
    return authValidation.validateToken(authToken)
        .flatMap(user -> customerRead.getCustomerData(customerId)
            .flatMap(customerExisting -> customerWrite.deleteCustomerData(customerId)
                .doOnNext(v ->
                    generalLog.logInfo("User: %s deleted customer with id: %s"
                        .formatted(user, customerId)))));
  }

  @Override
  public Flux<Customer> getAllCustomers(String authToken) {
    return authValidation.validateToken(authToken)
        .flatMapMany(user -> customerRead.getAllCustomersData());
  }
}
