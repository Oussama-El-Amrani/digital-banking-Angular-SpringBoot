package io.oussamaib0.banking.services;

import io.oussamaib0.banking.controller.dtos.auth.RegisterRequest;
import io.oussamaib0.banking.entities.AppUser;

public interface IUserService {
    AppUser registerUser(RegisterRequest registerRequest);
    AppUser registerAdmin(RegisterRequest registerRequest);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
