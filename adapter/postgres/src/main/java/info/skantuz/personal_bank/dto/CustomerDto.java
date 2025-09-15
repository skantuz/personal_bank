package info.skantuz.personal_bank.dto;

import info.skantuz.personal_bank.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
public class CustomerDto {
  @Id
  private String id;
  private String identificationType;
  private String identificationNumber;
  private String name;
  private String lastName;
  private String email;
  private LocalDate birthdate;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;


  public static CustomerDto of(Customer customer){
    return new CustomerDto(
            customer.id(),
            customer.identificationType(),
            customer.identificationNumber(),
            customer.name(),
            customer.lastName(),
            customer.email(),
            customer.birthdate(),
            customer.createdAt(),
            customer.updatedAt()
    );
  }

  public Customer toCustomerModel() {
    return new Customer(
            id,
            identificationType,
            identificationNumber,
            name,
            lastName,
            email,
            birthdate,
            createdAt,
            updatedAt
    );
  }

}
