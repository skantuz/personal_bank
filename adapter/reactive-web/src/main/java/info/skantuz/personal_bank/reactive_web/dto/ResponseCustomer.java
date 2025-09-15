package info.skantuz.personal_bank.reactive_web.dto;

import info.skantuz.personal_bank.model.Customer;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;


@Builder
@Getter
public class ResponseCustomer {
  private String id;
  private String identificationType;
  private String identificationNumber;
  private String name;
  private String lastName;
  private String email;
  private LocalDate birthdate;

  public static ResponseCustomer of(Customer customer) {
    return ResponseCustomer.builder()
        .id(customer.id())
        .identificationType(customer.identificationType())
        .identificationNumber(customer.identificationNumber())
        .name(customer.name())
        .lastName(customer.lastName())
        .birthdate(customer.birthdate())
        .email(customer.email())
        .build();
  }
}
