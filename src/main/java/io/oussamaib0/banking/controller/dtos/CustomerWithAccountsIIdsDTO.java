package io.oussamaib0.banking.controller.dtos;

import java.util.List;

public record CustomerWithAccountsIIdsDTO(
    Long id,
    String name,
    String email,
    String phoneNumber,
    List<String> accountIds
) {
}

