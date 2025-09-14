package info.skantuz.personal_bank.port.in;

import info.skantuz.personal_bank.model.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interface for customer-related operations.
 */
public interface CustomerIn {
    /**
     * Creates a new customer.
     *
     * @param customer the customer to create
     * @param authToken the authentication token for authorization
     * @return a Mono emitting the created customer
     */
    Mono<Customer> createCustomer(Customer customer, String authToken);
    Mono<Customer> getCustomerById(String customerId, String authToken);
    Mono<Customer> updateCustomer(String customerId, Customer customer, String authToken);
    Mono<String> deleteCustomer(String customerId, String authToken);
    Flux<Customer> getAllCustomers(String authToken);
}
