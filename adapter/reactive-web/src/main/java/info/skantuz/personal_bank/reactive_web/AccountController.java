package info.skantuz.personal_bank.reactive_web;

import info.skantuz.personal_bank.port.in.AccountIn;
import info.skantuz.personal_bank.reactive_web.dto.RequestAccount;
import info.skantuz.personal_bank.reactive_web.dto.ResponseAccount;
import info.skantuz.personal_bank.reactive_web.util.HttpUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "${server.context-path}/account")
public class AccountController {

  private final AccountIn accountIn;
  private final HttpUtils httpUtils;

  public AccountController(AccountIn accountIn, HttpUtils httpUtils) {
    this.accountIn = accountIn;
    this.httpUtils = httpUtils;
  }

  @GetMapping()
  @ResponseStatus(HttpStatus.OK)
  public Flux<ResponseAccount> getAccount(@RequestHeader MultiValueMap<String, String> header) {
    return httpUtils.validateToken(header)
        .flatMapMany(accountIn::getAllAccounts)
        .map(ResponseAccount::of);
  }

  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<ResponseAccount> createAccount(@RequestHeader MultiValueMap<String, String> header,
                                               @RequestBody RequestAccount account) {
    return httpUtils.validateToken(header)
        .flatMap(token -> accountIn.createAccount(account.getIdentificationType(),
            account.getIdentificationNumber(),account.toAccountBuilder(), token))
        .map(ResponseAccount::of);
  }

  @DeleteMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Mono<String> dropAccount(@RequestHeader MultiValueMap<String, String> header,
                                   @PathVariable String id) {
    return httpUtils.validateToken(header)
        .flatMap(token -> accountIn.deleteAccount(id, token));
  }

  @PatchMapping("/{id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Mono<ResponseAccount> partialUpdate(@RequestHeader MultiValueMap<String, String> header,
                                              @RequestBody RequestAccount account,
                                              @PathVariable String id) {
    return httpUtils.validateToken(header)
        .flatMap(token -> accountIn.updateAccount(id, account.toAccountModel(), token))
        .map(ResponseAccount::of);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Mono<ResponseAccount> Update(@RequestHeader MultiValueMap<String, String> header,
                                       @RequestBody RequestAccount account,
                                       @PathVariable String id) {
    return httpUtils.validateToken(header)
        .flatMap(token -> accountIn.updateAccount(id, account.toAccountModel(), token))
        .map(ResponseAccount::of);
  }

}
