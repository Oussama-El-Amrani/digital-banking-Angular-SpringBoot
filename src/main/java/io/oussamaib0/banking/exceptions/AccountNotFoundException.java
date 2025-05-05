package io.oussamaib0.banking.exceptions;

import java.util.UUID;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(UUID accountNumber) {
        super("Account with number " + accountNumber + " not found");
    }
}
