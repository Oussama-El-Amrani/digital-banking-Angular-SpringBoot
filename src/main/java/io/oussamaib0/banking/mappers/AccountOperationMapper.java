package io.oussamaib0.banking.mappers;

import io.oussamaib0.banking.controller.dtos.AccountOperationDTO;
import io.oussamaib0.banking.entities.AccountOperation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface AccountOperationMapper {
    @Mapping(source = "date", target = "operationDate")
    @Mapping(source = "operationType", target = "operationType")
    @Mapping(source = "bankAccount.id", target = "accountId")
    AccountOperationDTO toDTO(AccountOperation operation);

    default Date map(LocalDateTime localDateTime) {
        return localDateTime == null ? null : java.util.Date.from(localDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());
    }
}
