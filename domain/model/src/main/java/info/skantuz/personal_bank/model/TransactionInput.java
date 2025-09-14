package info.skantuz.personal_bank.model;

import info.skantuz.personal_bank.model.value_object.AccountType;
import info.skantuz.personal_bank.model.value_object.TransactionType;

import java.math.BigDecimal;

/**
 * Represents an incoming transaction with all required details.
 */
public record TransactionInput(
    TransactionType type,
    AccountType accountTypeWithdraw,
    AccountType accountTypeDeposit,
    String accountNumberWithdraw,
    String accountNumberDeposit,
    String identificationTypeWithdraw,
    String identificationTypeDeposit,
    String identificationNumberWithdraw,
    String identificationNumberDeposit,
    BigDecimal amount) {

  /**
   * Creates a new TransactionIn instance from the given parameters.
   *
   * @param accountTypeWithdraw               type of the origin account
   * @param accountTypeDeposit          type of the destination account
   * @param accountNumberWithdraw             number of the origin account
   * @param accountNumberDeposit        number of the destination account
   * @param identificationTypeWithdraw        identification type of the origin
   * @param identificationTypeDeposit   identification type of the destination
   * @param identificationNumberWithdraw      identification number of the origin
   * @param identificationNumberDeposit identification number of the destination
   * @param amount                          transaction amount
   * @return a new TransactionIn instance
   */
  public static TransactionInput of(
      TransactionType type,
      AccountType accountTypeWithdraw,
      AccountType accountTypeDeposit,
      String accountNumberWithdraw,
      String accountNumberDeposit,
      String identificationTypeWithdraw,
      String identificationTypeDeposit,
      String identificationNumberWithdraw,
      String identificationNumberDeposit,
      BigDecimal amount) {
    return new TransactionInput(
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

  /**
   * Returns a new Builder for constructing TransactionIn instances.
   *
   * @return a new Builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder for TransactionIn.
   */
  public static class Builder {
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

    /**
     * Sets the transaction type.
     *
     * @param type type of the transaction
     * @return this builder
     */
    public Builder type(TransactionType type) {
      this.type = type;
      return this;
    }

    /**
     * Sets the origin account type.
     *
     * @param accountTypeWithdraw type of the origin account
     * @return this builder
     */
    public Builder accountTypeWithdraw(AccountType accountTypeWithdraw) {
      this.accountTypeWithdraw = accountTypeWithdraw;
      return this;
    }

    /**
     * Sets the destination account type.
     *
     * @param accountTypeDeposit type of the destination account
     * @return this builder
     */
    public Builder accountTypeDeposit(AccountType accountTypeDeposit) {
      this.accountTypeDeposit = accountTypeDeposit;
      return this;
    }

    /**
     * Sets the origin account number.
     *
     * @param accountNumberWithdraw number of the origin account
     * @return this builder
     */
    public Builder accountNumberWithdraw(String accountNumberWithdraw) {
      this.accountNumberWithdraw = accountNumberWithdraw;
      return this;
    }

    /**
     * Sets the destination account number.
     *
     * @param accountNumberDeposit number of the destination account
     * @return this builder
     */
    public Builder accountNumberDeposit(String accountNumberDeposit) {
      this.accountNumberDeposit = accountNumberDeposit;
      return this;
    }

    /**
     * Sets the origin identification type.
     *
     * @param identificationTypeWithdraw identification type of the origin
     * @return this builder
     */
    public Builder identificationTypeWithdraw(String identificationTypeWithdraw) {
      this.identificationTypeWithdraw = identificationTypeWithdraw;
      return this;
    }

    /**
     * Sets the destination identification type.
     *
     * @param identificationTypeDeposit identification type of the destination
     * @return this builder
     */
    public Builder identificationTypeDeposit(String identificationTypeDeposit) {
      this.identificationTypeDeposit = identificationTypeDeposit;
      return this;
    }

    /**
     * Sets the origin identification number.
     *
     * @param identificationNumberWithdraw identification number of the origin
     * @return this builder
     */
    public Builder identificationNumberWithdraw(String identificationNumberWithdraw) {
      this.identificationNumberWithdraw = identificationNumberWithdraw;
      return this;
    }

    /**
     * Sets the destination identification number.
     *
     * @param identificationNumberDeposit identification number of the destination
     * @return this builder
     */
    public Builder identificationNumberDeposit(String identificationNumberDeposit) {
      this.identificationNumberDeposit = identificationNumberDeposit;
      return this;
    }

    /**
     * Sets the transaction amount.
     *
     * @param amount transaction amount
     * @return this builder
     */
    public Builder amount(BigDecimal amount) {
      this.amount = amount;
      return this;
    }

    /**
     * Builds a new TransactionIn instance.
     *
     * @return a new TransactionIn
     */
    public TransactionInput build() {
      return new TransactionInput(
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
}