-- Create the database
CREATE DATABASE "personal-bank";
\c personal-bank

-- Table: customers
CREATE TABLE customers (
    id VARCHAR PRIMARY KEY,
    identification_type VARCHAR NOT NULL,
    identification_number VARCHAR NOT NULL,
    name VARCHAR NOT NULL,
    last_name VARCHAR NOT NULL,
    email VARCHAR NOT NULL,
    birthdate DATE NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- Table: accounts
CREATE TABLE accounts (
    account_id VARCHAR PRIMARY KEY,
    account_type VARCHAR NOT NULL,
    account_number VARCHAR NOT NULL UNIQUE,
    status VARCHAR NOT NULL,
    balance NUMERIC(19,2) NOT NULL,
    exempt_gmf BOOLEAN NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    customer_id VARCHAR NOT NULL,
    CONSTRAINT fk_accounts_customer
        FOREIGN KEY (customer_id)
        REFERENCES customers(id)
        ON DELETE CASCADE
);

-- Table: transactions
CREATE TABLE transactions (
    id UUID PRIMARY KEY,
    type VARCHAR NOT NULL,
    account_type_withdraw VARCHAR,
    account_type_deposit VARCHAR,
    account_number_withdraw VARCHAR,
    account_number_deposit VARCHAR,
    amount NUMERIC(19,2) NOT NULL,
    CONSTRAINT fk_transactions_account_withdraw
        FOREIGN KEY (account_number_withdraw)
        REFERENCES accounts(account_number),
    CONSTRAINT fk_transactions_account_deposit
        FOREIGN KEY (account_number_deposit)
        REFERENCES accounts(account_number)
);