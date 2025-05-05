package io.oussamaib0.banking.controller.dtos;

import java.util.Date;
import java.util.UUID;

public record AccountOperationDTO(
    Long id,
    Date operationDate,
    Double amount,
    String operationType,
    String description,
    UUID accountId
) {
}
