package io.oussamaib0.banking.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "saving_accounts")
@NoArgsConstructor
public class SavingAccount extends BankAccount {
    private double interestRate;
    private double dailyWithdrawn;
}
