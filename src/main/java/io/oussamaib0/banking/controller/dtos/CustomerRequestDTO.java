package io.oussamaib0.banking.controller.dtos;

public record CustomerRequestDTO(
    String name,
    String email,
    String phoneNumber,
    String address
) {
}
