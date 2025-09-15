package info.skantuz.personal_bank.reactive_web;

import info.skantuz.personal_bank.port.in.CustomerIn;
import info.skantuz.personal_bank.reactive_web.dto.RequestCustomer;
import info.skantuz.personal_bank.reactive_web.dto.ResponseCustomer;
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
@RequestMapping(value = "${server.context-path}/customers")
public class CustomerController {

  private final CustomerIn customerIn;
  private final HttpUtils httpUtils;

  public CustomerController(CustomerIn customerIn, HttpUtils httpUtils) {
    this.customerIn = customerIn;
    this.httpUtils = httpUtils;
  }

  @GetMapping()
  @ResponseStatus(HttpStatus.OK)
  public Flux<ResponseCustomer> getCustomer(@RequestHeader MultiValueMap<String, String> header) {
    return httpUtils.validateToken(header)
        .flatMapMany(customerIn::getAllCustomers)
        .map(ResponseCustomer::of);
  }

  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<ResponseCustomer> createCustomer(@RequestHeader MultiValueMap<String, String> header,
                                               @RequestBody RequestCustomer customer) {
    return httpUtils.validateToken(header)
        .flatMap(token -> customerIn.createCustomer(customer.toCustomerModel(), token))
        .map(ResponseCustomer::of);
  }

  @DeleteMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Mono<String> dropCustomer(@RequestHeader MultiValueMap<String, String> header,
                                   @PathVariable String id) {
    return httpUtils.validateToken(header)
        .flatMap(token -> customerIn.deleteCustomer(id, token));
  }

  @PatchMapping("/{id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Mono<ResponseCustomer> partialUpdate(@RequestHeader MultiValueMap<String, String> header,
                                              @RequestBody RequestCustomer customer,
                                              @PathVariable String id) {
    return httpUtils.validateToken(header)
        .flatMap(token -> customerIn.updateCustomer(id, customer.toCustomerModel(), token))
        .map(ResponseCustomer::of);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Mono<ResponseCustomer> Update(@RequestHeader MultiValueMap<String, String> header,
                                       @RequestBody RequestCustomer customer,
                                       @PathVariable String id) {
    return httpUtils.validateToken(header)
        .flatMap(token -> customerIn.updateCustomer(id, customer.toCustomerModel(), token))
        .map(ResponseCustomer::of);
  }
}
