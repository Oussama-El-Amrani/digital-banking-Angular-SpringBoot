package io.oussamaib0.banking.services;

import io.oussamaib0.banking.entities.BankAccount;
import io.oussamaib0.banking.entities.Customer;
import io.oussamaib0.banking.exceptions.AccountNotFoundException;

import java.util.List;
import java.util.UUID;

public interface ICustomerAccountService {
    List<BankAccount> getCustomerAccounts(String username);
    BankAccount getCustomerAccount(String username, UUID accountId) throws AccountNotFoundException;
    void deposit(String username, UUID accountId, Double amount) throws AccountNotFoundException;
    void withdraw(String username, UUID accountId, Double amount) throws AccountNotFoundException;
    void transfer(String username, UUID sourceAccountId, UUID destinationAccountId, Double amount) throws AccountNotFoundException;
    Customer getCurrentCustomer(String username);
}
