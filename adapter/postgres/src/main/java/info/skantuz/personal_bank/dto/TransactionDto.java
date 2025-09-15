package info.skantuz.personal_bank.dto;

import info.skantuz.personal_bank.model.Transaction;
import info.skantuz.personal_bank.model.value_object.AccountType;
import info.skantuz.personal_bank.model.value_object.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transactions")
public class TransactionDto {
  @Id
  private UUID id;
  private TransactionType type;
  private AccountType accountTypeWithdraw;
  private AccountType accountTypeDeposit;
  private String accountNumberWithdraw;
  private String accountNumberDeposit;
  private BigDecimal amount;

  public static TransactionDto of(Transaction transaction) {
    return new TransactionDto(
        UUID.fromString(transaction.id()),
        transaction.type(),
        transaction.withdrawAccount().accountType(),
        transaction.depositAccount().accountType(),
        transaction.withdrawAccount().accountNumber(),
        transaction.depositAccount().accountNumber(),

        transaction.amount()
    );
  }
}
