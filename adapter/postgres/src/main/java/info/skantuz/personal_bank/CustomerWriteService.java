package info.skantuz.personal_bank;

import info.skantuz.personal_bank.dto.CustomerDto;
import info.skantuz.personal_bank.model.Customer;
import info.skantuz.personal_bank.port.out.CustomerWrite;
import info.skantuz.personal_bank.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerWriteService implements CustomerWrite {

  private final CustomerRepository repository;

  @Override
  public Mono<Customer> createCustomerData(Customer customer) {
    return repository.save(CustomerDto.of(customer))
        .map(CustomerDto::toCustomerModel);
  }

  @Override
  public Mono<Customer> updateCustomerData(String customerId, Customer customer) {
    return repository.findById(customerId)
        .flatMap(existingCustomer ->
            repository.save(updateCustomerDto(existingCustomer,customer)))
        .map(CustomerDto::toCustomerModel);
  }

  @Override
  public Mono<String> deleteCustomerData(String customerId) {
    return repository.deleteById(customerId)
        .thenReturn("Customer with ID " + customerId + " has been deleted.");
  }

  private CustomerDto updateCustomerDto(CustomerDto existingCustomer, Customer customer) {
    existingCustomer.setIdentificationType(customer.identificationType());
    existingCustomer.setIdentificationNumber(customer.identificationNumber());
    existingCustomer.setName(customer.name());
    existingCustomer.setLastName(customer.lastName());
    existingCustomer.setEmail(customer.email());
    existingCustomer.setBirthdate(customer.birthdate());
    existingCustomer.setUpdatedAt(customer.updatedAt());
    return existingCustomer;
  }
}
