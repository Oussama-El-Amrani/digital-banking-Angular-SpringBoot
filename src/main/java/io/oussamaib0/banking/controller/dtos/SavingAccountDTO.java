package io.oussamaib0.banking.controller.dtos;

import java.util.UUID;

public record SavingAccountDTO(
    UUID id,
    String accountNumber,
    String accountType,
    String currency,
    Double balance,
    Long customerId,
    String customerName,
    double interestRate,
    double dailyWithdrawn
) {
}
