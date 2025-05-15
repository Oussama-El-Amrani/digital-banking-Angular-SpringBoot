package io.oussamaib0.banking.controller;

import io.oussamaib0.banking.controller.dtos.*;
import io.oussamaib0.banking.entities.BankAccount;
import io.oussamaib0.banking.entities.CurrentAccount;
import io.oussamaib0.banking.entities.SavingAccount;
import io.oussamaib0.banking.exceptions.AccountNotFoundException;
import io.oussamaib0.banking.mappers.BankAccountMapper;
import io.oussamaib0.banking.services.IBankAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class BankAccountController {

    private final IBankAccountService bankAccountService;
    private final BankAccountMapper bankAccountMapper;

    public BankAccountController(IBankAccountService bankAccountService, BankAccountMapper bankAccountMapper) {
        this.bankAccountService = bankAccountService;
        this.bankAccountMapper = bankAccountMapper;
    }

    @PostMapping("/current")
    public ResponseEntity<BankAccountDTO> createCurrentAccount(@RequestBody CreateCurrentAccountRequest request) {
        BankAccount account = bankAccountService.createCurrentAccount(
            request.customerId(),
            request.initialBalance(),
            request.overdraftLimit()
        );
        return new ResponseEntity<>(mapToDTO(account), HttpStatus.CREATED);
    }

    @PostMapping("/saving")
    public ResponseEntity<BankAccountDTO> createSavingAccount(@RequestBody CreateSavingAccountRequest request) {
        BankAccount account = bankAccountService.createSavingAccount(
            request.customerId(),
            request.initialBalance(),
            request.interestRate()
        );
        return new ResponseEntity<>(mapToDTO(account), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankAccountDTO> getAccount(@PathVariable UUID id) {
        BankAccount account = bankAccountService.getAccount(id);
        return ResponseEntity.ok(mapToDTO(account));
    }

    @PostMapping("/deposit")
    public ResponseEntity<Void> deposit(@RequestBody TransactionRequest request) {
        try {
            bankAccountService.deposit(request.accountId(), request.amount());
            return ResponseEntity.ok().build();
        } catch (AccountNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdraw(@RequestBody TransactionRequest request) {
        try {
            bankAccountService.withdraw(request.accountId(), request.amount());
            return ResponseEntity.ok().build();
        } catch (AccountNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestBody TransferRequest request) {
        try {
            bankAccountService.transfer(
                request.sourceAccountId(),
                request.destinationAccountId(),
                request.amount()
            );
            return ResponseEntity.ok().build();
        } catch (AccountNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private BankAccountDTO mapToDTO(BankAccount account) {
        if (account instanceof CurrentAccount currentAccount) {
            CurrentAccountDTO dto = bankAccountMapper.currentAccountToDTO(currentAccount);
            return new BankAccountDTO(
                dto.id(),
                dto.accountNumber(),
                dto.accountType(),
                dto.currency(),
                dto.balance(),
                dto.customerId(),
                dto.customerName()
            );
        } else if (account instanceof SavingAccount savingAccount) {
            SavingAccountDTO dto = bankAccountMapper.savingAccountToDTO(savingAccount);
            return new BankAccountDTO(
                dto.id(),
                dto.accountNumber(),
                dto.accountType(),
                dto.currency(),
                dto.balance(),
                dto.customerId(),
                dto.customerName()
            );
        }
        return bankAccountMapper.bankAccountToDTO(account);
    }
}
