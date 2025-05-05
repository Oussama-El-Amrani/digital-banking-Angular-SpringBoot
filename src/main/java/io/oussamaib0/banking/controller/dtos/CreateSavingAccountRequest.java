package io.oussamaib0.banking.controller.dtos;

import java.math.BigDecimal;

public record CreateSavingAccountRequest(
    Long customerId,
    Double initialBalance,
    BigDecimal interestRate
) {
}
