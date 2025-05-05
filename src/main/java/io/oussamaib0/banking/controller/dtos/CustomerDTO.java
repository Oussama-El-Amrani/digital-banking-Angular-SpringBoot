package io.oussamaib0.banking.controller.dtos;

import java.util.List;

public record CustomerDTO(
    Long id,
    String name,
    String email,
    List<String> accountIds

) {
}
