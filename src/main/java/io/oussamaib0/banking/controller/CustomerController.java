package io.oussamaib0.banking.controller;

import io.oussamaib0.banking.controller.dtos.CustomerDTO;
import io.oussamaib0.banking.entities.Customer;
import io.oussamaib0.banking.services.ICustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;


@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final ICustomerService customerService;

    public CustomerController(ICustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.createCustomer(customer);
        CustomerDTO dto = mapToDTO(savedCustomer);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable Long id) {
        Customer customer = customerService.getCustomerById(id);
        CustomerDTO dto = mapToDTO(customer);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        Customer updatedCustomer = customerService.updateCustomer(id, customer);
        CustomerDTO dto = mapToDTO(updatedCustomer);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<CustomerDTO> getCustomerByEmail(@PathVariable String email) {
        Customer customer = customerService.getCustomerByEmail(email);
        CustomerDTO dto = mapToDTO(customer);
        return ResponseEntity.ok(dto);
    }

    private CustomerDTO mapToDTO(Customer customer) {
        return new CustomerDTO(
            customer.getId(),
            customer.getName(),
            customer.getEmail(),
            customer.getBankAccounts().stream()
                .map(account -> account.getId().toString())
                .collect(Collectors.toList())
        );
    }
}
