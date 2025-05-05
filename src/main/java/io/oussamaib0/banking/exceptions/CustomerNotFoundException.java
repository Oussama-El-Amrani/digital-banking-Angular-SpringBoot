package io.oussamaib0.banking.exceptions;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(Long customerId) {
        super(
            "Customer with ID " + customerId + " not found"
        );
    }
}
