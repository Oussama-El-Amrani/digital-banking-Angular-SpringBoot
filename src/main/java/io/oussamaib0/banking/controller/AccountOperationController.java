package io.oussamaib0.banking.controller;

import io.oussamaib0.banking.controller.dtos.AccountOperationDTO;
import io.oussamaib0.banking.entities.AccountOperation;
import io.oussamaib0.banking.mappers.AccountOperationMapper;
import io.oussamaib0.banking.services.IAccountOperationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/operations")
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class AccountOperationController {

    private final IAccountOperationService accountOperationService;
    private final AccountOperationMapper accountOperationMapper;

    public AccountOperationController(
        IAccountOperationService accountOperationService,
        AccountOperationMapper accountOperationMapper) {
        this.accountOperationService = accountOperationService;
        this.accountOperationMapper = accountOperationMapper;
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<List<AccountOperationDTO>> getAccountHistory(@PathVariable UUID accountId) {
        List<AccountOperation> operations = accountOperationService.getAccountHistory(accountId);
        List<AccountOperationDTO> dtos = operations.stream()
            .map(accountOperationMapper::toDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/interest/{accountId}")
    public ResponseEntity<Void> applyInterest(@PathVariable UUID accountId) {
        accountOperationService.applyInterest(accountId);
        return ResponseEntity.ok().build();
    }
}
