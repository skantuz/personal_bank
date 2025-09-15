package info.skantuz.personal_bank.reactive_web.dto;

import info.skantuz.personal_bank.model.TransactionInput;
import info.skantuz.personal_bank.model.value_object.AccountType;
import info.skantuz.personal_bank.model.value_object.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
  private TransactionType type;
  private AccountType accountTypeWithdraw;
  private AccountType accountTypeDeposit;
  private String accountNumberWithdraw;
  private String accountNumberDeposit;
  private String identificationTypeWithdraw;
  private String identificationTypeDeposit;
  private String identificationNumberWithdraw;
  private String identificationNumberDeposit;
  private BigDecimal amount;

  public TransactionInput toTransactionInput() {
    return TransactionInput.of(
        type,
        accountTypeWithdraw,
        accountTypeDeposit,
        accountNumberWithdraw,
        accountNumberDeposit,
        identificationTypeWithdraw,
        identificationTypeDeposit,
        identificationNumberWithdraw,
        identificationNumberDeposit,
        amount
    );
  }
}
