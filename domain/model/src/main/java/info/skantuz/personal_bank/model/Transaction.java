package info.skantuz.personal_bank.model;

    import info.skantuz.personal_bank.model.value_object.TransactionType;

    import java.math.BigDecimal;
    import java.time.LocalDateTime;

/**
     * Represents a financial transaction between accounts.
     *
     * @param id                 Unique identifier for the transaction.
     * @param originAccount      The account from which funds are debited (may be null for deposits).
     * @param destinationAccount The account to which funds are credited (may be null for withdrawals).
     * @param amount             The amount involved in the transaction.
     * @param type               The type of transaction (deposit, withdrawal, transfer).
     */
    public record Transaction(
            String id,
            Account originAccount,
            Account destinationAccount,
            BigDecimal amount,
            TransactionType type,
            LocalDateTime createdAt
    ) {
        /**
         * Generates a transaction with the specified parameters.
         *
         * @param originAccount      Account from which funds are debited (nullable).
         * @param destinationAccount Account to which funds are credited (nullable).
         * @param amount             Amount involved in the transaction.
         * @param type               Type of transaction.
         * @return a new {@code Transaction} instance.
         */
        private static Transaction generateTransaction(Account originAccount,
                                                       Account destinationAccount,
                                                       BigDecimal amount,
                                                       TransactionType type) {
            String id = java.util.UUID.randomUUID().toString();
            LocalDateTime createdAt = LocalDateTime.now();
            return new Transaction(id, originAccount, destinationAccount, amount, type, createdAt);
        }
        /**
         * Creates a deposit transaction.
         *
         * @param destinationAccount Account to deposit into.
         * @param amount             Amount to deposit.
         * @return a new deposit {@code Transaction}.
         */
        public static Transaction ofDeposit(Account destinationAccount, BigDecimal amount) {
            String id = java.util.UUID.randomUUID().toString();

            return generateTransaction(destinationAccount, null, amount, TransactionType.DEPOSIT);
        }

        /**
         * Creates a withdrawal transaction.
         *
         * @param originAccount Account to withdraw from.
         * @param amount        Amount to withdraw.
         * @return a new withdrawal {@code Transaction}.
         */
        public static Transaction ofWithdrawal(Account originAccount, BigDecimal amount) {
            return generateTransaction(originAccount, null, amount, TransactionType.WITHDRAWAL);
        }

        /**
         * Creates a transfer transaction.
         *
         * @param originAccount      Account to transfer from.
         * @param destinationAccount Account to transfer to.
         * @param amount             Amount to transfer.
         * @return a new transfer {@code Transaction}.
         */
        public static Transaction ofTransfer(Account originAccount, Account destinationAccount, BigDecimal amount) {
            return generateTransaction(originAccount, destinationAccount, amount, TransactionType.TRANSFER);
        }
    }