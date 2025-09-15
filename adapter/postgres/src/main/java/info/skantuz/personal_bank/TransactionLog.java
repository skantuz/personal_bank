package info.skantuz.personal_bank;

import info.skantuz.personal_bank.dto.TransactionDto;
import info.skantuz.personal_bank.model.Transaction;
import info.skantuz.personal_bank.port.out.TransactionsLog;
import info.skantuz.personal_bank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TransactionLog implements TransactionsLog {

  private final TransactionRepository repository;


  @Override
  public Mono<Void> logTransaction(Transaction transaction) {
    return repository.save(TransactionDto.of(transaction))
        .then();
  }
}
