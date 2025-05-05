package io.oussamaib0.banking.services.impl;

import io.oussamaib0.banking.entities.BankAccount;
import io.oussamaib0.banking.entities.CurrentAccount;
import io.oussamaib0.banking.entities.Customer;
import io.oussamaib0.banking.entities.SavingAccount;
import io.oussamaib0.banking.exceptions.AccountNotFoundException;
import io.oussamaib0.banking.repositories.BankAccountRepository;
import io.oussamaib0.banking.repositories.CustomerRepository;
import io.oussamaib0.banking.services.IBankAccountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class BankAccountService implements IBankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final CustomerRepository customerRepository;

    public BankAccountService(BankAccountRepository bankAccountRepository, CustomerRepository customerRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public BankAccount createCurrentAccount(Long ownerId, Double initialBalance, Double overdraftLimit) {
        Customer customer = customerRepository.findById(ownerId)
            .orElseThrow(() -> new RuntimeException("Customer not found"));

        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setCustomer(customer);
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverdraftLimit(overdraftLimit);
        currentAccount.setAccountType("CURRENT");
        currentAccount.setCurrency("USD");
        currentAccount.setAccountNumber(UUID.randomUUID().toString());

        return bankAccountRepository.save(currentAccount);
    }

    @Override
    public BankAccount createSavingAccount(Long ownerId, Double initialBalance, BigDecimal interestRate) {
        Customer customer = customerRepository.findById(ownerId)
            .orElseThrow(() -> new RuntimeException("Customer not found"));

        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setCustomer(customer);
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate.doubleValue());
        savingAccount.setAccountType("SAVING");
        savingAccount.setCurrency("USD");
        savingAccount.setAccountNumber(UUID.randomUUID().toString());

        return bankAccountRepository.save(savingAccount);
    }

    @Override
    public void deposit(UUID accountNumber, Double amount) throws AccountNotFoundException {
        BankAccount account = getAccount(accountNumber);
        account.setBalance(account.getBalance() + amount);
        bankAccountRepository.save(account);
    }

    @Override
    public void withdraw(UUID accountNumber, Double amount) throws AccountNotFoundException {
        BankAccount account = getAccount(accountNumber);

        if (account instanceof CurrentAccount currentAccount) {
            if (account.getBalance() + currentAccount.getOverdraftLimit() < amount) {
                throw new RuntimeException("Insufficient funds");
            }
        } else {
            if (account.getBalance() < amount) {
                throw new RuntimeException("Insufficient funds");
            }
        }

        account.setBalance(account.getBalance() - amount);
        bankAccountRepository.save(account);
    }

    @Override
    public void transfer(UUID source, UUID dest, Double amount) throws AccountNotFoundException {
        withdraw(source, amount);
        deposit(dest, amount);
    }

    @Override
    public BankAccount getAccount(UUID accountNumber) {
        return bankAccountRepository.findById(accountNumber)
            .orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }
}
