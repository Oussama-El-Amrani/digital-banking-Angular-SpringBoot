package io.oussamaib0.banking.services.impl;

import io.oussamaib0.banking.entities.AccountOperation;
import io.oussamaib0.banking.entities.BankAccount;
import io.oussamaib0.banking.entities.SavingAccount;
import io.oussamaib0.banking.entities.enums.OperationType;
import io.oussamaib0.banking.exceptions.AccountNotFoundException;
import io.oussamaib0.banking.repositories.AccountOperationRepository;
import io.oussamaib0.banking.repositories.BankAccountRepository;
import io.oussamaib0.banking.services.IAccountOperationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class AccountOperationService implements IAccountOperationService {

    private final AccountOperationRepository accountOperationRepository;
    private final BankAccountRepository bankAccountRepository;

    public AccountOperationService(
        AccountOperationRepository accountOperationRepository,
        BankAccountRepository bankAccountRepository) {
        this.accountOperationRepository = accountOperationRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public List<AccountOperation> getAccountHistory(UUID accountNumber) {
        BankAccount bankAccount = bankAccountRepository.findById(accountNumber)
            .orElseThrow(() -> new AccountNotFoundException(accountNumber));

        return bankAccount.getTransactions();
    }

    @Override
    public void applyInterest(UUID accountNumber) {
        BankAccount bankAccount = bankAccountRepository.findById(accountNumber)
            .orElseThrow(() -> new AccountNotFoundException(accountNumber));

        if (bankAccount instanceof SavingAccount savingAccount) {
            double interestAmount = savingAccount.getBalance() * savingAccount.getInterestRate() / 100;
            savingAccount.setBalance(savingAccount.getBalance() + interestAmount);

            // Create an interest operation record
            AccountOperation interestOperation = new AccountOperation();
            interestOperation.setBankAccount(savingAccount);
            interestOperation.setAmount(interestAmount);
            interestOperation.setOperationType(OperationType.INTEREST);
            interestOperation.setDescription("Interest applied at rate: " + savingAccount.getInterestRate() + "%");

            accountOperationRepository.save(interestOperation);
            bankAccountRepository.save(savingAccount);
        }
    }
}
