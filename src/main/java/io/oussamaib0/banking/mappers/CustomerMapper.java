package io.oussamaib0.banking.mappers;

import io.oussamaib0.banking.controller.dtos.CustomerDTO;
import io.oussamaib0.banking.controller.dtos.CustomerRequestDTO;
import io.oussamaib0.banking.controller.dtos.CustomerWithAccountsIIdsDTO;
import io.oussamaib0.banking.entities.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = {Collectors.class, UUID.class})
public interface CustomerMapper {
    @Mapping(target = "accountIds", expression = "java(customer.getBankAccounts().stream().map(account -> account.getId().toString()).collect(Collectors.toList()))")
    CustomerWithAccountsIIdsDTO toWithAccountsIdsDTO(Customer customer);

    CustomerDTO toDTO(Customer customer);

    @Mapping(target = "id", source = "id")
    Customer toEntity(CustomerDTO customerDTO);

    Customer fromRequestToEntity(CustomerRequestDTO customerDTO);
}
