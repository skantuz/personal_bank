package info.skantuz.model;

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
 * @param lastname             Customer's last name.
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
        String lastname,
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
     * @param lastname             Last name.
     * @param email                Email address.
     * @param birthdate            Date of birth.
     * @param createdAt            Creation timestamp.
     * @param updatedAt            Last update timestamp.
     * @return a new {@code Customer} instance.
     */
    public static Customer of(String id,
                              String identificationType,
                              String identificationNumber,
                              String name,
                              String lastname,
                              String email,
                              LocalDate birthdate,
                              LocalDateTime createdAt,
                              LocalDateTime updatedAt) {
        return new Customer(id, identificationType, identificationNumber,
                name, lastname, email, birthdate, createdAt, updatedAt);
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
        private String lastname;
        private String email;
        private java.time.LocalDate birthdate;
        private java.time.LocalDateTime createdAt;
        private java.time.LocalDateTime updatedAt;

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
         * @param lastname the last name.
         * @return this builder.
         */
        public CustomerBuilder lastname(String lastname) {
            this.lastname = lastname;
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
        public CustomerBuilder birthdate(java.time.LocalDate birthdate) {
            this.birthdate = birthdate;
            return this;
        }

        /**
         * Sets the creation timestamp.
         *
         * @param createdAt the creation date and time.
         * @return this builder.
         */
        public CustomerBuilder createdAt(java.time.LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        /**
         * Sets the last update timestamp.
         *
         * @param updatedAt the last update date and time.
         * @return this builder.
         */
        public CustomerBuilder updatedAt(java.time.LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        /**
         * Builds a new {@link Customer} instance with the configured values.
         *
         * @return a new {@code Customer}.
         */
        public Customer build() {
            return new Customer(id, identificationType, identificationNumber,
                    name, lastname, email, birthdate, createdAt, updatedAt);
        }
    }
}