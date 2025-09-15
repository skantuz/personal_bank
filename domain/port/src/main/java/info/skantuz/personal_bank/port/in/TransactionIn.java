package info.skantuz.personal_bank.port.in;

import info.skantuz.personal_bank.model.TransactionInput;
import reactor.core.publisher.Mono;

/**
 * Interface for handling transfer operations into the system.
 */
public interface TransactionIn {

  Mono<String> transaction(TransactionInput transactionInput, String authToken);
}
