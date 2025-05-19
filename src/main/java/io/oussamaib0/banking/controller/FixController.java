package io.oussamaib0.banking.controller;

import io.oussamaib0.banking.entities.AppUser;
import io.oussamaib0.banking.entities.Customer;
import io.oussamaib0.banking.repositories.AppUserRepository;
import io.oussamaib0.banking.repositories.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/fix")
public class FixController {

    private final AppUserRepository userRepository;
    private final CustomerRepository customerRepository;

    public FixController(AppUserRepository userRepository, CustomerRepository customerRepository) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
    }

    @GetMapping("/create-customer-profiles")
    public ResponseEntity<String> createCustomerProfiles() {
        int count = 0;
        
        // Get all users
        Iterable<AppUser> users = userRepository.findAll();
        
        for (AppUser user : users) {
            // Check if user already has a customer profile
            if (user.getCustomer() == null) {
                // Create a new customer profile
                Customer customer = new Customer();
                customer.setName(user.getUsername() + " (Auto-created)");
                customer.setEmail(user.getUsername() + "@example.com");
                customer.setPhoneNumber("");
                customer.setAddress("");
                customer.setBankAccounts(new ArrayList<>());
                customer.setUser(user);
                
                // Save the customer
                customerRepository.save(customer);
                count++;
            }
        }
        
        return ResponseEntity.ok("Created " + count + " customer profiles");
    }
    
    @GetMapping("/fix-root-user")
    public ResponseEntity<String> fixRootUser() {
        // Find the root user
        Optional<AppUser> rootUserOpt = userRepository.findByUsername("root");
        
        if (rootUserOpt.isEmpty()) {
            return ResponseEntity.ok("Root user not found");
        }
        
        AppUser rootUser = rootUserOpt.get();
        
        // Check if root user already has a customer profile
        if (rootUser.getCustomer() != null) {
            return ResponseEntity.ok("Root user already has a customer profile");
        }
        
        // Create a new customer profile for root user
        Customer customer = new Customer();
        customer.setName("Root Administrator");
        customer.setEmail("root@example.com");
        customer.setPhoneNumber("");
        customer.setAddress("");
        customer.setBankAccounts(new ArrayList<>());
        customer.setUser(rootUser);
        
        // Save the customer
        customerRepository.save(customer);
        
        return ResponseEntity.ok("Created customer profile for root user");
    }
}
