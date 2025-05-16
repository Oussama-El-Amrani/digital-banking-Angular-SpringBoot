package io.oussamaib0.banking.controller;

import io.oussamaib0.banking.controller.dtos.CustomerDTO;
import io.oussamaib0.banking.controller.dtos.CustomerRequestDTO;
import io.oussamaib0.banking.entities.Customer;
import io.oussamaib0.banking.mappers.CustomerMapper;
import io.oussamaib0.banking.services.ICustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final ICustomerService customerService;
    private final CustomerMapper mapper;

    public CustomerController(ICustomerService customerService, CustomerMapper customerMapper) {
        this.customerService = customerService;
        this.mapper = customerMapper;
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerRequestDTO requestDTO) {
        var entity = mapper.fromRequestToEntity(requestDTO);
        Customer savedCustomer = customerService.createCustomer(entity);
        CustomerDTO dto = mapper.toDTO(savedCustomer);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<?>> getAllCustomers(@RequestParam(defaultValue = "false") boolean includeAccounts) {
        if (includeAccounts) {
            List<Customer> customers = customerService.getAllCustomersWitAccounts();
            return ResponseEntity.ok(customers.stream()
                .map(customer -> {
                    List<String> accountIds = customer.getBankAccounts().stream()
                        .map(account -> account.getId().toString())
                        .collect(Collectors.toList());
                    return mapper.toWithAccountsIdsDTO(customer);
                })
                .collect(Collectors.toList()));
        } else {
            List<Customer> customers = customerService.getAllCustomers();
            return ResponseEntity.ok(customers.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomer(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean includeAccounts) {
        if (includeAccounts) {
            Customer customer = customerService.getCustomerByIdWithAccounts(id);
            List<String> accountIds = customer.getBankAccounts().stream()
                .map(account -> account.getId().toString())
                .collect(Collectors.toList());

            return ResponseEntity.ok(mapper.toWithAccountsIdsDTO(customer));
        } else {
            Customer customer = customerService.getCustomerById(id);
            CustomerDTO dto = mapper.toDTO(customer);
            return ResponseEntity.ok(dto);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @RequestBody CustomerRequestDTO requestDTO) {
        var entity = mapper.fromRequestToEntity(requestDTO);
        Customer updatedCustomer = customerService.updateCustomer(id, entity);
        CustomerDTO dto = mapper.toDTO(updatedCustomer);
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
        CustomerDTO dto = mapper.toDTO(customer);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}/accounts")
    public ResponseEntity<List<String>> getCustomerAccountIds(@PathVariable Long id) {
        Customer customer = customerService.getCustomerAccountIds(id);
        List<String> accountIds = customer.getBankAccounts().stream()
            .map(account -> account.getId().toString())
            .collect(Collectors.toList());
        return ResponseEntity.ok(accountIds);
    }

}
