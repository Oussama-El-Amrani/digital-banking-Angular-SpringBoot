package io.oussamaib0.banking.services;

import io.oussamaib0.banking.entities.AccountOperation;

import java.util.List;
import java.util.UUID;

public interface IAccountOperationService {
    List<AccountOperation> getAccountHistory(UUID accountNumber);

    void applyInterest(UUID accountNumber);
}
