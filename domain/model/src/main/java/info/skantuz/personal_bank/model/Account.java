package info.skantuz.personal_bank.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a bank account with its details and status.
 *
 * @param id             Unique identifier for the account.
 * @param accountType    Type of the account (e.g., savings, checking).
 * @param accountNumber  Account number.
 * @param status         Account status (active, inactive, cancelled).
 * @param balance        Current account balance.
 * @param exemptGMF      Whether the account is exempt from GMF tax.
 * @param createdAt      Date and time when the account was created.
 * @param updatedAt      Date and time when the account was last updated.
 * @param customerId         Identifier of the customer who owns the account.
 */
public record Account(
        String id,
        AccountType accountType,
        String accountNumber,
        String status,
        BigDecimal balance,
        boolean exemptGMF,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String customerId
) {
    /**
     * Static factory method to create an {@code Account} instance.
     *
     * @param id             Unique identifier.
     * @param accountType    Type of the account.
     * @param accountNumber  Account number.
     * @param status         Account status.
     * @param balance        Account balance.
     * @param exemptGMF      Exemption from GMF.
     * @param createdAt      Creation timestamp.
     * @param updatedAt      Last update timestamp.
     * @param customerId         Owner customer ID.
     * @return a new {@code Account} instance.
     */
    public static Account of(String id,
                             AccountType accountType,
                             String accountNumber,
                             String status,
                             BigDecimal balance,
                             boolean exemptGMF,
                             LocalDateTime createdAt,
                             LocalDateTime updatedAt,
                             String customerId) {
        return new Account(id, accountType, accountNumber, status, balance, exemptGMF, createdAt, updatedAt, customerId);
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
     * Builder class for constructing {@link Account} instances.
     */
    public static class AccountBuilder {
        private String id;
        private AccountType accountType;
        private String accountNumber;
        private String status;
        private BigDecimal balance;
        private boolean exemptGMF;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String customerId;

        private AccountBuilder() {}

        /**
         * Sets the account ID.
         * @param id the account ID.
         * @return this builder.
         */
        public AccountBuilder id(String id) {
            this.id = id;
            return this;
        }

        /**
         * Sets the account type.
         * @param accountType the account type.
         * @return this builder.
         */
        public AccountBuilder accountType(AccountType accountType) {
            this.accountType = accountType;
            return this;
        }

        /**
         * Sets the account number.
         * @param accountNumber the account number.
         * @return this builder.
         */
        public AccountBuilder accountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        /**
         * Sets the account status.
         * @param status the account status.
         * @return this builder.
         */
        public AccountBuilder status(String status) {
            this.status = status;
            return this;
        }

        /**
         * Sets the account balance.
         * @param balance the account balance.
         * @return this builder.
         */
        public AccountBuilder balance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        /**
         * Sets whether the account is exempt from GMF.
         * @param exemptGMF exemption flag.
         * @return this builder.
         */
        public AccountBuilder exemptGMF(boolean exemptGMF) {
            this.exemptGMF = exemptGMF;
            return this;
        }

        /**
         * Sets the creation timestamp.
         * @param createdAt creation date and time.
         * @return this builder.
         */
        public AccountBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        /**
         * Sets the last update timestamp.
         * @param updatedAt last update date and time.
         * @return this builder.
         */
        public AccountBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        /**
         * Sets the customer ID who owns the account.
         * @param customerId the customer ID.
         * @return this builder.
         */
        public AccountBuilder customerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        /**
         * Builds a new {@link Account} instance.
         * @return a new {@code Account}.
         */
        public Account build() {
            return new Account(id, accountType, accountNumber, status, balance, exemptGMF, createdAt, updatedAt, customerId);
        }
    }
}