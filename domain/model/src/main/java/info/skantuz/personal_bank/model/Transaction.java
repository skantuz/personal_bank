package info.skantuz.personal_bank.model;

    import info.skantuz.personal_bank.model.value_object.TransactionType;

    import java.math.BigDecimal;
    import java.time.LocalDateTime;

/**
     * Represents a financial transaction between accounts.
     *
     * @param id                 Unique identifier for the transaction.
     * @param withdrawAccount      The account from which funds are debited (may be null for deposits).
     * @param depositAccount The account to which funds are credited (may be null for withdrawals).
     * @param amount             The amount involved in the transaction.
     * @param type               The type of transaction (deposit, withdrawal, transfer).
     */
    public record Transaction(
            String id,
            Account withdrawAccount,
            Account depositAccount,
            BigDecimal amount,
            TransactionType type,
            LocalDateTime createdAt,
            String authLog

    ) {
        /**
         * Generates a transaction with the specified parameters.
         *
         * @param withdrawAccount      Account from which funds are debited (nullable).
         * @param depositAccount Account to which funds are credited (nullable).
         * @param amount             Amount involved in the transaction.
         * @param type               Type of transaction.
         * @return a new {@code Transaction} instance.
         */
        private static Transaction generateTransaction(Account withdrawAccount,
                                                       Account depositAccount,
                                                       BigDecimal amount,
                                                       TransactionType type,
                                                       String authLog) {
            String id = java.util.UUID.randomUUID().toString();
            LocalDateTime createdAt = LocalDateTime.now();
            return new Transaction(id, withdrawAccount, depositAccount, amount, type, createdAt, authLog);
        }
        /**
         * Creates a deposit transaction.
         *
         * @param depositAccount Account to deposit into.
         * @param amount             Amount to deposit.
         * @return a new deposit {@code Transaction}.
         */
        public static Transaction ofDeposit(Account depositAccount, BigDecimal amount, String authLog) {
            String id = java.util.UUID.randomUUID().toString();
            return generateTransaction(null, depositAccount, amount, TransactionType.DEPOSIT, authLog);
        }

        /**
         * Creates a withdrawal transaction.
         *
         * @param withdrawAccount Account to withdraw from.
         * @param amount        Amount to withdraw.
         * @return a new withdrawal {@code Transaction}.
         */
        public static Transaction ofWithdrawal(Account withdrawAccount, BigDecimal amount, String authLog) {
            return generateTransaction(withdrawAccount, null, amount, TransactionType.WITHDRAWAL, authLog);
        }

        /**
         * Creates a transfer transaction.
         *
         * @param withdrawAccount      Account to transfer from.
         * @param depositAccount Account to transfer to.
         * @param amount             Amount to transfer.
         * @return a new transfer {@code Transaction}.
         */
        public static Transaction ofTransfer(Account withdrawAccount, Account depositAccount, BigDecimal amount,
                                             String authLog) {
            return generateTransaction(withdrawAccount, depositAccount, amount, TransactionType.TRANSFER, authLog);
        }
    }