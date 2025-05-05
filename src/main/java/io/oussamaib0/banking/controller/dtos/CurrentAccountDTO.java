package io.oussamaib0.banking.controller.dtos;

import java.util.UUID;

public record CurrentAccountDTO(
    UUID id,
    String accountNumber,
    String accountType,
    String currency,
    Double balance,
    Long customerId,
    String customerName,
    double overdraftLimit
) {
}
