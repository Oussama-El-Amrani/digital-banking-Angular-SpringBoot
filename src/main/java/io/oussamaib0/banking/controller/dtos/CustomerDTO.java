package io.oussamaib0.banking.controller.dtos;

public record CustomerDTO(
    Long id,
    String name,
    String email,
    String phoneNumber

) {
}
