package info.skantuz.personal_bank.port.in;

import info.skantuz.personal_bank.model.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Marker interface for account-related input operations.
 */
public interface AccountIn {

    /** Creates a new account.
     *
     * @param account the account to create
     * @param authToken the authentication token for authorization
     * @return a Mono emitting the created account
     */
    Mono<Account> createAccount(Account account, String authToken);

    /** Retrieves an account by its ID.
     *
     * @param accountId the ID of the account to retrieve
     * @param authToken the authentication token for authorization
     * @return a Mono emitting the retrieved account
     */
    Mono<Account> getAccountById(String accountId, String authToken);

    /** Updates an existing account.
     *
     * @param accountId the ID of the account to update
     * @param account the updated account data
     * @param authToken the authentication token for authorization
     * @return a Mono emitting the updated account
     */
    Mono<Account> updateAccount(String accountId, Account account, String authToken);

    /**
     * Deletes an account by its ID.
     * @param accountId
     * @param authToken
     * @return
     */
    Mono<String> deleteAccount(String accountId, String authToken);

    /** Retrieves all accounts.
     *
     * @param authToken the authentication token for authorization
     * @return a Flux emitting all accounts
     */
    Flux<Account> getAllAccounts(String authToken);
}
