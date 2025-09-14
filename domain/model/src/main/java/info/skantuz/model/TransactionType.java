package info.skantuz.model;

/**
 * Enum representing the types of financial transactions.
 * <ul>
 *   <li>{@link #DEPOSIT} - Deposit transaction.</li>
 *   <li>{@link #WITHDRAWAL} - Withdrawal transaction.</li>
 *   <li>{@link #TRANSFER} - Transfer transaction.</li>
 * </ul>
 */
public enum TransactionType {
    /**
     * Deposit transaction type.
     */
    DEPOSIT,

    /**
     * Withdrawal transaction type.
     */
    WITHDRAWAL,
    /**
     * Transfer transaction
     * type.
     */
    TRANSFER
}
