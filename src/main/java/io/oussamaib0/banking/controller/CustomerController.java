package io.oussamaib0.banking.controller;

import io.oussamaib0.banking.controller.dtos.CustomerDTO;
import io.oussamaib0.banking.controller.dtos.CustomerRequestDTO;
import io.oussamaib0.banking.entities.AppUser;
import io.oussamaib0.banking.entities.Customer;
import io.oussamaib0.banking.mappers.CustomerMapper;
import io.oussamaib0.banking.repositories.AppUserRepository;
import io.oussamaib0.banking.services.ICustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/customers")
@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class CustomerController {

    private final ICustomerService customerService;
    private final CustomerMapper mapper;
    private final AppUserRepository userRepository;

    public CustomerController(ICustomerService customerService, CustomerMapper customerMapper, AppUserRepository userRepository) {
        this.customerService = customerService;
        this.mapper = customerMapper;
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerRequestDTO requestDTO) {
        try {
            var entity = mapper.fromRequestToEntity(requestDTO);
            Customer savedCustomer = customerService.createCustomer(entity);
            CustomerDTO dto = mapper.toDTO(savedCustomer);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<List<?>> getAllCustomers(@RequestParam(defaultValue = "false") boolean includeAccounts) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomer(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean includeAccounts) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Customer not found or error occurred");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @RequestBody CustomerRequestDTO requestDTO) {
        try {
        var entity = mapper.fromRequestToEntity(requestDTO);
        Customer updatedCustomer = customerService.updateCustomer(id, entity);
        CustomerDTO dto = mapper.toDTO(updatedCustomer);
        return ResponseEntity.ok(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        try {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<CustomerDTO> getCustomerByEmail(@PathVariable String email) {
        try {
        Customer customer = customerService.getCustomerByEmail(email);
        CustomerDTO dto = mapper.toDTO(customer);
        return ResponseEntity.ok(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/accounts")
    public ResponseEntity<List<String>> getCustomerAccountIds(@PathVariable Long id) {
        try {
        Customer customer = customerService.getCustomerAccountIds(id);
        List<String> accountIds = customer.getBankAccounts().stream()
            .map(account -> account.getId().toString())
            .collect(Collectors.toList());
        return ResponseEntity.ok(accountIds);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentCustomer(@RequestParam(defaultValue = "false") boolean includeAccounts) {
        try {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getCustomer() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User is not associated with a customer");
        }

        Customer customer = user.getCustomer();

        if (includeAccounts) {
            customer = customerService.getCustomerByIdWithAccounts(customer.getId());
            return ResponseEntity.ok(mapper.toWithAccountsIdsDTO(customer));
        } else {
            return ResponseEntity.ok(mapper.toDTO(customer));
        }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving customer information");
        }
    }
}
