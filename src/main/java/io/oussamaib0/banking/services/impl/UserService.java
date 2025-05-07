package io.oussamaib0.banking.services.impl;

import io.oussamaib0.banking.controller.dtos.auth.RegisterRequest;
import io.oussamaib0.banking.entities.AppRole;
import io.oussamaib0.banking.entities.AppUser;
import io.oussamaib0.banking.entities.Customer;
import io.oussamaib0.banking.repositories.AppRoleRepository;
import io.oussamaib0.banking.repositories.AppUserRepository;
import io.oussamaib0.banking.repositories.CustomerRepository;
import io.oussamaib0.banking.services.IUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService implements IUserService {

    private final AppUserRepository userRepository;
    private final AppRoleRepository roleRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder encoder;

    public UserService(AppUserRepository userRepository, AppRoleRepository roleRepository,
                       CustomerRepository customerRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.customerRepository = customerRepository;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public AppUser registerUser(RegisterRequest registerRequest) {
        // Create new user's account
        AppUser user = AppUser.builder()
                .username(registerRequest.getUsername())
                .password(encoder.encode(registerRequest.getPassword()))
                .roles(new HashSet<>())
                .build();

        Set<String> strRoles = registerRequest.getRoles();
        Set<AppRole> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            AppRole userRole = roleRepository.findByName("CUSTOMER")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                AppRole appRole = roleRepository.findByName(role)
                        .orElseThrow(() -> new RuntimeException("Error: Role " + role + " is not found."));
                roles.add(appRole);
            });
        }

        user.setRoles(roles);
        AppUser savedUser = userRepository.save(user);

        // Create customer profile for the user
        Customer customer = new Customer();
        customer.setName(registerRequest.getName());
        customer.setEmail(registerRequest.getEmail());
        customer.setPhoneNumber(registerRequest.getPhoneNumber());
        customer.setAddress(registerRequest.getAddress());
        customer.setBankAccounts(new ArrayList<>());
        customer.setUser(savedUser);
        
        customerRepository.save(customer);
        
        return savedUser;
    }

    @Override
    @Transactional
    public AppUser registerAdmin(RegisterRequest registerRequest) {
        // Create new admin account
        AppUser user = AppUser.builder()
                .username(registerRequest.getUsername())
                .password(encoder.encode(registerRequest.getPassword()))
                .roles(new HashSet<>())
                .build();

        Set<AppRole> roles = new HashSet<>();
        AppRole adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("Error: Admin Role is not found."));
        roles.add(adminRole);

        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return customerRepository.findByEmail(email).isPresent();
    }
}
