package io.oussamaib0.banking.controller.dtos;

public record CreateCurrentAccountRequest(
    Long customerId,
    Double initialBalance,
    Double overdraftLimit
) {
}
