package io.oussamaib0.banking.services.impl;

import io.oussamaib0.banking.entities.Customer;
import io.oussamaib0.banking.exceptions.CustomerEmailNotFoundException;
import io.oussamaib0.banking.exceptions.CustomerNotFoundException;
import io.oussamaib0.banking.repositories.CustomerRepository;
import io.oussamaib0.banking.services.ICustomerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService implements ICustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer createCustomer(Customer customer) {
        if (customer.getBankAccounts() == null) {
            customer.setBankAccounts(new ArrayList<>());
        }
        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
            .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @Override
    public Customer updateCustomer(Long id, Customer customer) {
        Customer existingCustomer = getCustomerById(id);

        if (customer.getName() != null) {
            existingCustomer.setName(customer.getName());
        }
        if (customer.getEmail() != null) {
            existingCustomer.setEmail(customer.getEmail());
        }
        if (customer.getPhoneNumber() != null) {
            existingCustomer.setPhoneNumber(customer.getPhoneNumber());
        }
        if (customer.getAddress() != null) {
            existingCustomer.setAddress(customer.getAddress());
        }

        return customerRepository.save(existingCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException(id);
        }
        customerRepository.deleteById(id);
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
            .orElseThrow(() -> new CustomerEmailNotFoundException(email));
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public List<Customer> getAllCustomersWitAccounts() {
        return customerRepository.findAllWithAccounts();
    }

    @Override
    public Customer getCustomerByIdWithAccounts(Long id) {
        return customerRepository.findByIdWithAccounts(id)
            .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @Override
    public Customer getCustomerAccountIds(Long id) {
        return customerRepository.findByIdWithAccounts(id).orElseThrow(
            () -> new CustomerNotFoundException(id)
        );
    }
}
