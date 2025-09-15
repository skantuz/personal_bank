package info.skantuz.personal_bank;

import info.skantuz.personal_bank.dto.CustomerDto;
import info.skantuz.personal_bank.model.Customer;
import info.skantuz.personal_bank.port.out.CustomerRead;
import info.skantuz.personal_bank.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerReadService  implements CustomerRead {

  private final CustomerRepository repository;

  @Override
  public Mono<Customer> getCustomerData(String customerId) {
    return repository.findById(customerId)
        .map(CustomerDto::toCustomerModel);
  }

  @Override
  public Mono<Customer> getCustomerByDocumentTypeAndNumber(String documentType, String documentNumber) {
    return repository.findByDocumentTypeAndDocumentNumber(documentType, documentNumber)
        .map(CustomerDto::toCustomerModel);
  }

  @Override
  public Flux<Customer> getAllCustomersData() {
    return repository.findAll()
        .map(CustomerDto::toCustomerModel);
  }
}
