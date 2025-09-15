package info.skantuz.personal_bank.reactive_web.dto;

import info.skantuz.personal_bank.model.Customer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestCustomer {

  private String identificationType;
  private String identificationNumber;
  private String name;
  private String lastName;
  @NotBlank(message = "El correo electrónico no puede estar vacío")
  @Email(message = "El formato del correo electrónico no es válido")
  private String email;
  private LocalDate birthdate;

  public Customer toCustomerModel() {
    return Customer.builder()
        .identificationType(identificationType)
        .identificationNumber(identificationNumber)
        .name(name)
        .lastName(lastName)
        .birthdate(birthdate)
        .email(email)
        .build();
  }

}
