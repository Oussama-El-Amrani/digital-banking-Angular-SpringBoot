package io.oussamaib0.banking.services.impl;

import io.oussamaib0.banking.entities.AppUser;
import io.oussamaib0.banking.entities.BankAccount;
import io.oussamaib0.banking.entities.Customer;
import io.oussamaib0.banking.entities.CurrentAccount;
import io.oussamaib0.banking.exceptions.AccountNotFoundException;
import io.oussamaib0.banking.repositories.AppUserRepository;
import io.oussamaib0.banking.repositories.BankAccountRepository;
import io.oussamaib0.banking.services.ICustomerAccountService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerAccountService implements ICustomerAccountService {

    private final AppUserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;

    public CustomerAccountService(AppUserRepository userRepository, BankAccountRepository bankAccountRepository) {
        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public List<BankAccount> getCustomerAccounts(String username) {
        Customer customer = getCurrentCustomer(username);
        return customer.getBankAccounts();
    }

    @Override
    public BankAccount getCustomerAccount(String username, UUID accountId) throws AccountNotFoundException {
        Customer customer = getCurrentCustomer(username);
        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
        
        if (!account.getCustomer().getId().equals(customer.getId())) {
            throw new AccessDeniedException("Account does not belong to this customer");
        }
        
        return account;
    }

    @Override
    @Transactional
    public void deposit(String username, UUID accountId, Double amount) throws AccountNotFoundException {
        BankAccount account = getCustomerAccount(username, accountId);
        account.setBalance(account.getBalance() + amount);
        bankAccountRepository.save(account);
    }

    @Override
    @Transactional
    public void withdraw(String username, UUID accountId, Double amount) throws AccountNotFoundException {
        BankAccount account = getCustomerAccount(username, accountId);
        
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
    @Transactional
    public void transfer(String username, UUID sourceAccountId, UUID destinationAccountId, Double amount) throws AccountNotFoundException {
        BankAccount sourceAccount = getCustomerAccount(username, sourceAccountId);
        BankAccount destinationAccount = bankAccountRepository.findById(destinationAccountId)
                .orElseThrow(() -> new AccountNotFoundException(destinationAccountId));
        
        if (sourceAccount instanceof CurrentAccount currentAccount) {
            if (sourceAccount.getBalance() + currentAccount.getOverdraftLimit() < amount) {
                throw new RuntimeException("Insufficient funds");
            }
        } else {
            if (sourceAccount.getBalance() < amount) {
                throw new RuntimeException("Insufficient funds");
            }
        }
        
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);
        
        bankAccountRepository.save(sourceAccount);
        bankAccountRepository.save(destinationAccount);
    }

    @Override
    public Customer getCurrentCustomer(String username) {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (user.getCustomer() == null) {
            throw new RuntimeException("User is not associated with a customer");
        }
        
        return user.getCustomer();
    }
}
