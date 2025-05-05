package io.oussamaib0.banking.exceptions;

public class CustomerEmailNotFoundException extends RuntimeException {
    public CustomerEmailNotFoundException(String email) {
        super(
            "Customer with Email " + email + " not found"
        );
    }
}
