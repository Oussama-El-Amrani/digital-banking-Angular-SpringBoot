package io.oussamaib0.banking.controller.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private List<String> roles;
    private Long customerId;

    public JwtResponse(String token, Long id, String username, List<String> roles, Long customerId) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.roles = roles;
        this.customerId = customerId;
    }
}
