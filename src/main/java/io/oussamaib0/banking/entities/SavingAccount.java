package io.oussamaib0.banking.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "saving_accounts")
@NoArgsConstructor
@Data
public class SavingAccount extends BankAccount {
    private double interestRate;
    private double dailyWithdrawn;
}
