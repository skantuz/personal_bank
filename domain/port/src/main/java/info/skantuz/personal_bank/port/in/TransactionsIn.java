package info.skantuz.personal_bank.port.in;

import info.skantuz.personal_bank.model.TransactionInput;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

/**
 * Interface for handling transfer operations into the system.
 */
public interface TransactionsIn {

  Mono<String> transaction(TransactionInput transactionInput, String authToken);
}
