package io.oussamaib0.banking.controller;

import io.oussamaib0.banking.controller.dtos.BankAccountDTO;
import io.oussamaib0.banking.controller.dtos.TransactionRequest;
import io.oussamaib0.banking.controller.dtos.TransferRequest;
import io.oussamaib0.banking.entities.BankAccount;
import io.oussamaib0.banking.exceptions.AccountNotFoundException;
import io.oussamaib0.banking.mappers.BankAccountMapper;
import io.oussamaib0.banking.services.ICustomerAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customer/accounts")
@PreAuthorize("hasRole('CUSTOMER')")
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class CustomerAccountController {

    private final ICustomerAccountService customerAccountService;
    private final BankAccountMapper bankAccountMapper;

    public CustomerAccountController(ICustomerAccountService customerAccountService, BankAccountMapper bankAccountMapper) {
        this.customerAccountService = customerAccountService;
        this.bankAccountMapper = bankAccountMapper;
    }

    @GetMapping
    public ResponseEntity<List<BankAccountDTO>> getMyAccounts() {
        try {
            String username = getCurrentUsername();
            List<BankAccount> accounts = customerAccountService.getCustomerAccounts(username);
            List<BankAccountDTO> dtos = accounts.stream()
                    .map(bankAccountMapper::bankAccountToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankAccountDTO> getMyAccount(@PathVariable UUID id) {
        try {
            String username = getCurrentUsername();
            BankAccount account = customerAccountService.getCustomerAccount(username, id);
            return ResponseEntity.ok(bankAccountMapper.bankAccountToDTO(account));
        } catch (AccountNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<Void> deposit(@RequestBody TransactionRequest request) {
        try {
            String username = getCurrentUsername();
            customerAccountService.deposit(username, request.accountId(), request.amount());
            return ResponseEntity.ok().build();
        } catch (AccountNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdraw(@RequestBody TransactionRequest request) {
        try {
            String username = getCurrentUsername();
            customerAccountService.withdraw(username, request.accountId(), request.amount());
            return ResponseEntity.ok().build();
        } catch (AccountNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("Insufficient funds")) {
                return ResponseEntity.badRequest().build();
            }
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestBody TransferRequest request) {
        try {
            String username = getCurrentUsername();
            customerAccountService.transfer(
                    username,
                    request.sourceAccountId(),
                    request.destinationAccountId(),
                    request.amount()
            );
            return ResponseEntity.ok().build();
        } catch (AccountNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("Insufficient funds")) {
                return ResponseEntity.badRequest().build();
            }
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
