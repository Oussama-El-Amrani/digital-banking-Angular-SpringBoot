package io.oussamaib0.banking.mappers;

import io.oussamaib0.banking.controller.dtos.BankAccountDTO;
import io.oussamaib0.banking.controller.dtos.CurrentAccountDTO;
import io.oussamaib0.banking.controller.dtos.SavingAccountDTO;
import io.oussamaib0.banking.entities.BankAccount;
import io.oussamaib0.banking.entities.CurrentAccount;
import io.oussamaib0.banking.entities.SavingAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BankAccountMapper {
    BankAccountDTO bankAccountToDTO(BankAccount bankAccount);

    CurrentAccountDTO currentAccountToDTO(CurrentAccount currentAccount);

    SavingAccountDTO savingAccountToDTO(SavingAccount savingAccount);
}
