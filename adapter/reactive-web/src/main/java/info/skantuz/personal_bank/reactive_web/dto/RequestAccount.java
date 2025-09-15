package info.skantuz.personal_bank.reactive_web.dto;

import info.skantuz.personal_bank.model.Account;
import info.skantuz.personal_bank.model.value_object.AccountState;
import info.skantuz.personal_bank.model.value_object.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestAccount {

  private String id;
  private AccountType accountType;
  private String accountNumber;
  private AccountState status;
  private BigDecimal balance;
  private boolean exemptGMF;
  private String customerId;
  private String IdentificationType;
  private String IdentificationNumber;



  public Account.AccountBuilder toAccountBuilder() {
    return Account.builder()
        .accountType(accountType)
        .status(status)
        .balance(balance)
        .exemptGMF(exemptGMF);
  }

  public Account toAccountModel() {
    return Account.builder()
        .id(id)
        .accountType(accountType)
        .status(status)
        .balance(balance)
        .exemptGMF(exemptGMF)
        .build(accountNumber, customerId);
  }
}
