package io.oussamaib0.banking.services;

import io.oussamaib0.banking.entities.Customer;

public interface ICustomerService {
    Customer createCustomer(Customer customer);

    Customer getCustomerById(Long id);

    Customer updateCustomer(Long id, Customer customer);

    void deleteCustomer(Long id);

    Customer getCustomerByEmail(String email);
}
