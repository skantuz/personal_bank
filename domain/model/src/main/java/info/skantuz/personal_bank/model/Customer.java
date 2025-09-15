package info.skantuz.personal_bank.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a customer with identification and personal information.
 * <p>
 * Immutable data structure for storing customer details.
 * </p>
 *
 * @param id                   Unique identifier for the customer.
 * @param identificationType   Type of identification (e.g., passport, ID card).
 * @param identificationNumber Identification number.
 * @param name                 Customer's first name.
 * @param lastName             Customer's last name.
 * @param email                Customer's email address.
 * @param birthdate            Customer's date of birth.
 * @param createdAt            Date and time when the customer was created.
 * @param updatedAt            Date and time when the customer was last updated.
 */
public record Customer(
        String id,
        String identificationType,
        String identificationNumber,
        String name,
        String lastName,
        String email,
        LocalDate birthdate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    /**
     * Creates a new {@code Customer} instance with the specified attributes.
     *
     * @param id                   Unique identifier.
     * @param identificationType   Type of identification.
     * @param identificationNumber Identification number.
     * @param name                 First name.
     * @param lastName             Last name.
     * @param email                Email address.
     * @param birthdate            Date of birth.
     * @return a new {@code Customer} instance.
     */
    public static Customer of(String id,
                              String identificationType,
                              String identificationNumber,
                              String name,
                              String lastName,
                              String email,
                              LocalDate birthdate) {
        return new Customer(id, identificationType, identificationNumber,
                name, lastName, email, birthdate, LocalDateTime.now(), LocalDateTime.now());
    }

    /**
     * Returns a new {@code Customer} with the updated identification type and refreshed updatedAt.
     *
     * @param identificationType the new identification type
     * @return a new {@code Customer} instance
     */
    public Customer setIdentificationType(String identificationType) {
        return new Customer(
            this.id(),
            identificationType,
            this.identificationNumber(),
            this.name(),
            this.lastName(),
            this.email(),
            this.birthdate(),
            this.createdAt(),
            LocalDateTime.now()
        );
    }

    /**
     * Returns a new {@code Customer} with the updated identification number and refreshed updatedAt.
     *
     * @param identificationNumber the new identification number
     * @return a new {@code Customer} instance
     */
    public Customer setIdentificationNumber(String identificationNumber) {
        return new Customer(
            this.id(),
            this.identificationType(),
            identificationNumber,
            this.name(),
            this.lastName(),
            this.email(),
            this.birthdate(),
            this.createdAt(),
            LocalDateTime.now()
        );
    }

    /**
     * Returns a new {@code Customer} with the updated name and refreshed updatedAt.
     *
     * @param name the new name
     * @return a new {@code Customer} instance
     */
    public Customer setName(String name) {
        return new Customer(
            this.id(),
            this.identificationType(),
            this.identificationNumber(),
            name,
            this.lastName(),
            this.email(),
            this.birthdate(),
            this.createdAt(),
            LocalDateTime.now()
        );
    }

    /**
     * Returns a new {@code Customer} with the updated lastname and refreshed updatedAt.
     *
     * @param lastName the new lastname
     * @return a new {@code Customer} instance
     */
    public Customer setLastName(String lastName) {
        return new Customer(
            this.id(),
            this.identificationType(),
            this.identificationNumber(),
            this.name(),
            lastName,
            this.email(),
            this.birthdate(),
            this.createdAt(),
            LocalDateTime.now()
        );
    }

    /**
     * Returns a new {@code Customer} with the updated email and refreshed updatedAt.
     *
     * @param email the new email address
     * @return a new {@code Customer} instance
     */
    public Customer setEmail(String email) {
        return new Customer(
            this.id(),
            this.identificationType(),
            this.identificationNumber(),
            this.name(),
            this.lastName(),
            email,
            this.birthdate(),
            this.createdAt(),
            LocalDateTime.now()
        );
    }

    /**
     * Returns a new {@link CustomerBuilder} instance for building a {@code Customer}.
     *
     * @return a new {@code CustomerBuilder}.
     */
    public static CustomerBuilder builder() {
        return new CustomerBuilder();
    }

    /**
     * Builder class for constructing {@link Customer} instances.
     */
    public static class CustomerBuilder {
        private String id;
        private String identificationType;
        private String identificationNumber;
        private String name;
        private String lastName;
        private String email;
        private LocalDate birthdate;

        private CustomerBuilder() {
        }

        /**
         * Sets the unique identifier.
         *
         * @param id the customer ID.
         * @return this builder.
         */
        public CustomerBuilder id(String id) {
            this.id = id;
            return this;
        }

        /**
         * Sets the identification type.
         *
         * @param identificationType the type of identification.
         * @return this builder.
         */
        public CustomerBuilder identificationType(String identificationType) {
            this.identificationType = identificationType;
            return this;
        }

        /**
         * Sets the identification number.
         *
         * @param identificationNumber the identification number.
         * @return this builder.
         */
        public CustomerBuilder identificationNumber(String identificationNumber) {
            this.identificationNumber = identificationNumber;
            return this;
        }

        /**
         * Sets the first name.
         *
         * @param name the first name.
         * @return this builder.
         */
        public CustomerBuilder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the last name.
         *
         * @param lastName the last name.
         * @return this builder.
         */
        public CustomerBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        /**
         * Sets the email address.
         *
         * @param email the email address.
         * @return this builder.
         */
        public CustomerBuilder email(String email) {
            this.email = email;
            return this;
        }

        /**
         * Sets the date of birth.
         *
         * @param birthdate the date of birth.
         * @return this builder.
         */
        public CustomerBuilder birthdate(LocalDate birthdate) {
            this.birthdate = birthdate;
            return this;
        }

        /**
         * Builds a new {@link Customer} instance with the configured values.
         *
         * @return a new {@code Customer}.
         */
        public Customer build() {
            return Customer.of(id, identificationType, identificationNumber,
                    name, lastName, email, birthdate);
        }
    }
}