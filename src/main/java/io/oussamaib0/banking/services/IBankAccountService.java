package io.oussamaib0.banking.services;

import io.oussamaib0.banking.entities.BankAccount;
import io.oussamaib0.banking.exceptions.AccountNotFoundException;

import java.math.BigDecimal;
import java.util.UUID;

public interface IBankAccountService {
    BankAccount createCurrentAccount(Long ownerId, Double initialBalance, Double overdraftLimit);

    BankAccount createSavingAccount(Long ownerId, Double initialBalance, BigDecimal interestRate);

    void deposit(UUID accountNumber, Double amount) throws AccountNotFoundException;

    void withdraw(UUID accountNumber, Double amount) throws AccountNotFoundException;

    void transfer(UUID source, UUID dest, Double amount) throws AccountNotFoundException;

    BankAccount getAccount(UUID accountNumber);
}
