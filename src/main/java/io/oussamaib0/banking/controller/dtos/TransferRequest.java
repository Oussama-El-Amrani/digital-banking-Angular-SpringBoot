package io.oussamaib0.banking.controller.dtos;

import java.util.UUID;

public record TransferRequest(
    UUID sourceAccountId,
    UUID destinationAccountId,
    Double amount,
    String description
) {
}
