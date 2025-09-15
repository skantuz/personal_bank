package info.skantuz.personal_bank.repository;

import info.skantuz.personal_bank.dto.CustomerDto;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface CustomerRepository extends R2dbcRepository<CustomerDto, String> {

  Mono<CustomerDto> findByDocumentTypeAndDocumentNumber(String documentType, String documentNumber);
}
