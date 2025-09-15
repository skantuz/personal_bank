package info.skantuz.personal_bank.model;

import info.skantuz.personal_bank.model.value_object.AccountState;
import info.skantuz.personal_bank.model.value_object.AccountType;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static info.skantuz.personal_bank.model.value_object.AccountType.CHECKING;

/**
 * Represents a bank account with its details and status.
 *
 * @param id            Unique identifier for the account.
 * @param accountType   Type of the account (e.g., savings, checking).
 * @param accountNumber Account number.
 * @param status        Account status (active, inactive, cancelled).
 * @param balance       Current account balance.
 * @param exemptGMF     Whether the account is exempt from GMF tax.
 * @param createdAt     Date and time when the account was created.
 * @param updatedAt     Date and time when the account was last updated.
 * @param customerId    Identifier of the customer who owns the account.
 */
public record Account(
    String id,
    AccountType accountType,
    String accountNumber,
    AccountState status,
    BigDecimal balance,
    boolean exemptGMF,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String customerId
) {

  /**
   * Static factory method to create an {@code Account} instance.
   *
   * @param accountType   Type of the account.
   * @param accountNumber Account number.
   * @param status        Account status.
   * @param balance       Account balance.
   * @param exemptGMF     Exemption from GMF.
   * @param customerId    Owner customer ID.
   * @return a new {@code Account} instance.
   */
  public static Account of(AccountType accountType,
                           String accountNumber,
                           AccountState status,
                           BigDecimal balance,
                           boolean exemptGMF,
                           String customerId) {

    return new Account(UUID.randomUUID().toString(), accountType, accountNumber, status,
        balance, exemptGMF, LocalDateTime.now(), LocalDateTime.now(), customerId);
  }

  /**
   * Static factory method to create an {@code Account} instance.
   *
   * @param accountId    Unique identifier for the account.
   * @param accountType   Type of the account.
   * @param accountNumber Account number.
   * @param status        Account status.
   * @param balance       Account balance.
   * @param exemptGMF     Exemption from GMF.
   * @param customerId    Owner customer ID.
   * @return a new {@code Account} instance.
   */
  public static Account of(String accountId,
                           AccountType accountType,
                           String accountNumber,
                           AccountState status,
                           BigDecimal balance,
                           boolean exemptGMF,
                           String customerId) {

    if (accountId == null || accountId.isEmpty()) {
          accountId = UUID.randomUUID().toString();
    }
    return new Account(accountId, accountType, accountNumber, status, balance,
        exemptGMF, LocalDateTime.now(), LocalDateTime.now(), customerId);
  }

  /**
   * Returns a new {@link AccountBuilder} for building an {@code Account}.
   *
   * @return a new {@code AccountBuilder}.
   */
  public static AccountBuilder builder() {
    return new AccountBuilder();
  }

  /**
   * Returns a new {@code Account} with the updated account type and refreshed updatedAt.
   *
   * @param accountNumber the new account type
   * @return a new {@code Account} instance
   */
  public Account setAccountNumber(String accountNumber) {
    return new Account(this.id, this.accountType, accountNumber, this.status, this.balance,
        this.exemptGMF, this.createdAt, this.updatedAt, this.customerId);
  }

  /**
   * Returns a new {@code Account} with the updated status and refreshed updatedAt.
   *
   * @param status the new account status
   * @return a new {@code Account} instance
   */
  public Account setStatus(AccountState status) {
    return new Account(this.id, this.accountType, this.accountNumber, status, this.balance,
        this.exemptGMF, this.createdAt, this.updatedAt, this.customerId);
  }

  /**
   * Returns a new {@code Account} with Deposit and refreshed updatedAt.
   *
   * @param amount the amount to deposit
   * @return a new {@code Account} instance
   */
  public Account deposit(BigDecimal amount) {
    BigDecimal newBalance = this.balance.add(amount);
    return new Account(this.id, this.accountType, this.accountNumber, this.status, newBalance,
        this.exemptGMF, this.createdAt, LocalDateTime.now(), this.customerId);
  }

  public boolean canWithdraw(BigDecimal amount) {
    return this.balance.compareTo(amount) >= 0 || this.accountType.equals(CHECKING);
  }

  /**
   * Returns a new {@code Account} with Withdrawal and refreshed updatedAt.
   *
   * @param amount the amount to withdraw
   * @return a new {@code Account} instance
   */
  public Account withdraw(BigDecimal amount) {
    BigDecimal newBalance = this.balance.subtract(amount);
    return new Account(this.id, this.accountType, this.accountNumber, this.status, newBalance,
        this.exemptGMF, this.createdAt, LocalDateTime.now(), this.customerId);
  }

  /**
   * Returns a new {@code Account} with the updated balance and refreshed updatedAt.
   *
   * @param balance the new account balance
   * @return a new {@code Account} instance
   */
  public Account setBalance(BigDecimal balance) {
    return new Account(this.id, this.accountType, this.accountNumber, this.status, balance,
        this.exemptGMF, this.createdAt, this.updatedAt, this.customerId);
  }

  public boolean isActive() {
    return this.status == AccountState.ACTIVE;
  }

  /**
   * Builder class for constructing {@link Account} instances.
   */
  public static class AccountBuilder {

    /**
     * -- GETTER --
     * Get Account Type
     *
     * @return account type
     */
    @Getter
    private AccountType accountType;
    private AccountState status;
    private BigDecimal balance;
    private boolean exemptGMF;
    private String accountId;

    private AccountBuilder() {
    }

    /**
     * Sets the account type.
     *
     * @param accountType the account type.
     * @return this builder.
     */
    public AccountBuilder accountType(AccountType accountType) {
      this.accountType = accountType;
      return this;
    }

    /**
     * Sets the account status.
     *
     * @param status the account status.
     * @return this builder.
     */
    public AccountBuilder status(AccountState status) {
      this.status = status;
      return this;
    }

    /**
     * Sets the account balance.
     *
     * @param balance the account balance.
     * @return this builder.
     */
    public AccountBuilder balance(BigDecimal balance) {
      this.balance = balance;
      return this;
    }

    /**
     * Sets whether the account is exempt from GMF.
     *
     * @param exemptGMF exemption flag.
     * @return this builder.
     */
    public AccountBuilder exemptGMF(boolean exemptGMF) {
      this.exemptGMF = exemptGMF;
      return this;
    }

    /**
     * Builds a new {@link Account} instance.
     *
     * @return a new {@code Account}.
     */
    public Account build(String accountNumber, String customerId) {
      return Account.of(accountId,accountType, accountNumber, status, balance, exemptGMF, customerId);
    }

    /**
     * Sets the account ID.
     *
     * @param id the account ID.
     * @return this builder.
     */
    public AccountBuilder id(String id) {
      this.accountId = id;
      return this;
    }
  }
}