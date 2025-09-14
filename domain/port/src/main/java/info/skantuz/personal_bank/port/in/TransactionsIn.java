package info.skantuz.personal_bank.port.in;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;

/**
 * Interface for handling transfer operations into the system.
 */
public interface TransactionsIn {
    /**
     * Deposits a specified amount into the given account.
     *
     * @param accountId the ID of the account to transfer funds into
     * @param amount the amount to transfer
     * @param authToken the authentication token for authorization
     * @return a boolean indicating whether the transfer was successful
     */
    Mono<String> depositToAccount(String accountId, BigDecimal amount, String authToken);

    /**
     * Withdraws a specified amount from the given account.
     *
     * @param accountId the ID of the account to withdraw funds from
     * @param amount the amount to withdraw
     * @param authToken the authentication token for authorization
     */
    Mono<String> withdrawFromAccount(String accountId, BigDecimal amount, String authToken);

    /**
     * Transfers a specified amount from one account to another.
     *
     * @param fromAccountId the ID of the account to transfer funds from
     * @param toAccountId the ID of the account to transfer funds to
     * @param amount the amount to transfer
     * @param authToken the authentication token for authorization
     * @return a boolean indicating whether the transfer was successful
     */
    Mono<String> transferBetweenAccounts(String fromAccountId, String toAccountId, String identificationType,
                                         String identification, BigDecimal amount, String authToken);

}
