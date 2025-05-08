package io.oussamaib0.banking.config;

import io.oussamaib0.banking.controller.dtos.auth.RegisterRequest;
import io.oussamaib0.banking.entities.AppRole;
import io.oussamaib0.banking.repositories.AppRoleRepository;
import io.oussamaib0.banking.services.IUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class DatabaseInitializer {

    @Bean
    CommandLineRunner initDatabase(AppRoleRepository roleRepository, IUserService userService) {
        return args -> {
            // Initialize roles if they don't exist
            if (roleRepository.findByName("ADMIN").isEmpty()) {
                roleRepository.save(new AppRole(null, "ADMIN"));
            }

            if (roleRepository.findByName("CUSTOMER").isEmpty()) {
                roleRepository.save(new AppRole(null, "CUSTOMER"));
            }

            // Create default admin user if it doesn't exist
            if (!userService.existsByUsername("admin")) {
                RegisterRequest adminRequest = new RegisterRequest();
                adminRequest.setUsername("admin");
                adminRequest.setPassword("admin123");
                adminRequest.setName("Administrator");
                adminRequest.setEmail("admin@example.com");

                Set<String> roles = new HashSet<>();
                roles.add("ADMIN");
                adminRequest.setRoles(roles);

                userService.registerAdmin(adminRequest);
            }

            // Create root admin user if it doesn't exist
            if (!userService.existsByUsername("root")) {
                RegisterRequest rootRequest = new RegisterRequest();
                rootRequest.setUsername("root");
                rootRequest.setPassword("root");
                rootRequest.setName("Root Administrator");
                rootRequest.setEmail("root@example.com");

                Set<String> roles = new HashSet<>();
                roles.add("ADMIN");
                rootRequest.setRoles(roles);

                userService.registerAdmin(rootRequest);
                System.out.println("Root admin user created successfully: username=root, password=root");
            }
        };
    }
}
