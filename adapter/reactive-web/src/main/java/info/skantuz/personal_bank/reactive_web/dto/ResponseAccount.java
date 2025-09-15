package info.skantuz.personal_bank.reactive_web.dto;

import info.skantuz.personal_bank.model.Account;
import info.skantuz.personal_bank.model.value_object.AccountState;
import info.skantuz.personal_bank.model.value_object.AccountType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder(toBuilder = true)
public class ResponseAccount {
  private String id;
  private AccountType accountType;
  private String accountNumber;
  private AccountState status;
  private BigDecimal balance;
  private boolean exemptGMF;

  private String customerId;

  public static ResponseAccount of(Account account){
    return ResponseAccount.builder()
        .id(account.id())
        .accountType(account.accountType())
        .accountNumber(account.accountNumber())
        .status(account.status())
        .balance(account.balance())
        .exemptGMF(account.exemptGMF())
        .customerId(account.customerId())
        .build();
  }
}
