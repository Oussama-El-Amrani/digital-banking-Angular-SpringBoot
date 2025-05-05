package io.oussamaib0.banking.controller.dtos;

import java.util.UUID;

public record TransactionRequest(
    UUID accountId,
    Double amount,
    String description
) {
}
