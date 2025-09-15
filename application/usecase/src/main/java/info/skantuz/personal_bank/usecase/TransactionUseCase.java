package info.skantuz.personal_bank.usecase;


import info.skantuz.personal_bank.model.Account;
import info.skantuz.personal_bank.model.Transaction;
import info.skantuz.personal_bank.model.TransactionInput;
import info.skantuz.personal_bank.model.error.ErrorList;
import info.skantuz.personal_bank.port.in.TransactionIn;
import info.skantuz.personal_bank.port.out.AccountRead;
import info.skantuz.personal_bank.port.out.AccountWrite;
import info.skantuz.personal_bank.port.out.AuthValidation;
import info.skantuz.personal_bank.port.out.CustomerRead;
import info.skantuz.personal_bank.port.out.GeneralLog;
import info.skantuz.personal_bank.port.out.TransactionsLog;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class TransactionUseCase implements TransactionIn {

  private final CustomerRead customerRead;
  private final AccountRead accountRead;
  private final AccountWrite accountWrite;
  private final AuthValidation authValidation;
  private final GeneralLog generalLog;
  private final TransactionsLog transactionsLog;

  @Override
  public Mono<String> transaction(TransactionInput transactionInput, String authToken) {
    return authValidation.validateToken(authToken)
        .flatMap(user -> {
          switch (transactionInput.type()) {
            case DEPOSIT -> {
              return processDeposit(transactionInput, user)
                  .map(account -> "Depósito realizado con éxito nuevo saldo: " + account.balance());
            }
            case WITHDRAWAL -> {
              return processWithdrawal(transactionInput, user)
                  .map(account -> "Retiro realizado con éxito nuevo saldo: " + account.balance());
            }
            case TRANSFER -> {
              return processWithdrawal(transactionInput, user)
                  .flatMap(account -> processDeposit(transactionInput, user))
                  .map( account -> "Transferencia realizada con éxito nuevo saldo: " + account.balance());
            }
          }
          return null;
        });
  }

  private Mono<Account> processDeposit(TransactionInput transactionInput, String user) {
    return Mono.zip(customerRead.getCustomerByDocumentTypeAndNumber(
                transactionInput.identificationTypeDeposit(), transactionInput.identificationNumberDeposit()),
            accountRead.getAccountByAccountNumberAndType(transactionInput.accountNumberDeposit(),
                transactionInput.accountTypeDeposit()))
        .map(tuple -> {
          if (tuple.getT1().id().equals(tuple.getT2().customerId()) && tuple.getT2().isActive() ) {
            var account = tuple.getT2();
            var amount = transactionInput.amount();
            return Transaction.ofDeposit(account.deposit(amount), amount, user);
          }
          throw ErrorList.ACCOUNT_NOT_FOUND.toApiException(
              "The account does not belong to the customer");
        })
        .doOnNext(transaction -> generalLog.logInfo("%s id %s user : %s"
            .formatted(transaction.type(), transaction.id(), user)))
        .doOnNext(transactionsLog::logTransaction)
        .flatMap(transaction ->
            accountWrite.updateAccountData(transaction.depositAccount().id(),
                    transaction.depositAccount()));
  }

  private Mono<Account> processWithdrawal(TransactionInput transactionInput, String user) {
    return Mono.zip(customerRead.getCustomerByDocumentTypeAndNumber(
                transactionInput.identificationTypeWithdraw(), transactionInput.identificationNumberWithdraw()),
            accountRead.getAccountByAccountNumberAndType(transactionInput.accountNumberWithdraw(),
                transactionInput.accountTypeWithdraw()))
        .map(tuple -> {
          if (tuple.getT1().id().equals(tuple.getT2().customerId()) && tuple.getT2().isActive() ) {
            var account = tuple.getT2();
            var amount = transactionInput.amount();
            if (account.canWithdraw(amount)) {
              return Transaction.ofWithdrawal(account.withdraw(amount), amount, user);
            } else {
              throw ErrorList.INSUFFICIENT_FUNDS.toApiException(
                  "Insufficient funds for %s".formatted(transactionInput.type()));
            }
          }
          throw ErrorList.ACCOUNT_NOT_FOUND.toApiException(
              "The account does not belong to the customer");
        })
        .doOnNext(transaction -> generalLog.logInfo("%s id %s user : %s"
            .formatted(transaction.type(), transaction.id(), user)))
        .doOnNext(transactionsLog::logTransaction)
        .flatMap(transaction ->
            accountWrite.updateAccountData(transaction.withdrawAccount().id(),
                    transaction.withdrawAccount()));
  }

}
