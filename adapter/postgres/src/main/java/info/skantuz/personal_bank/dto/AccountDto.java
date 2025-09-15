package info.skantuz.personal_bank.dto;

import info.skantuz.personal_bank.model.Account;
import info.skantuz.personal_bank.model.value_object.AccountState;
import info.skantuz.personal_bank.model.value_object.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@Table(name = "accounts")
public class AccountDto {
  private String accountId;
  private AccountType accountType;
  private String accountNumber;
  private AccountState status;
  private BigDecimal balance;
  private boolean exemptGMF;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String customerId;

  public static AccountDto of(Account accountData) {
    return new AccountDto(
        accountData.id(),
        accountData.accountType(),
        accountData.accountNumber(),
        accountData.status(),
        accountData.balance(),
        accountData.exemptGMF(),
        accountData.createdAt(),
        accountData.updatedAt(),
        accountData.customerId()
    );
  }

  public Account toAccountModel() {
    return new Account(
        accountId,
        accountType,
        accountNumber,
        status,
        balance,
        exemptGMF,
        createdAt,
        updatedAt,
        customerId
    );
  }
}
