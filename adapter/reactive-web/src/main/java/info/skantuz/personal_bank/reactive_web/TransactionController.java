package info.skantuz.personal_bank.reactive_web;

import info.skantuz.personal_bank.port.in.TransactionIn;
import info.skantuz.personal_bank.reactive_web.dto.TransactionRequest;
import info.skantuz.personal_bank.reactive_web.util.HttpUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "${server.context-path}/transaction")
public class TransactionController {

  private final TransactionIn transactionIn;
  private final HttpUtils httpUtils;

  public TransactionController(TransactionIn transactionIn, HttpUtils httpUtils) {
    this.transactionIn = transactionIn;
    this.httpUtils = httpUtils;
  }

  @PostMapping
  public Mono<String> createTransaction(@RequestHeader MultiValueMap<String,String> header,
                                        @RequestBody TransactionRequest transactionRequest) {
    return httpUtils.validateToken(header)
        .flatMap(token -> transactionIn.transaction(transactionRequest.toTransactionInput(),token));

  }
}
