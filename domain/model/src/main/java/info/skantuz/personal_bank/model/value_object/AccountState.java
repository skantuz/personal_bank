package info.skantuz.personal_bank.model.value_object;

/**
 * Enum representing the states of a bank account.
 * <ul>
 *   <li>{@link #ACTIVE} - Active account.</li>
 *   <li>{@link #INACTIVE} - Inactive account.</li>
 *   <li>{@link #CLOSED} - Closed account.</li>
 * </ul>
 */
public enum AccountState {
    /**
     * Active account state.
     */
    ACTIVE,

    /**
     * Inactive account state.
     */
    INACTIVE,

    /**
     * Closed account state.
     */
    CLOSED
}
