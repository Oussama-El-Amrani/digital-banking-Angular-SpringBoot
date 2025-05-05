package io.oussamaib0.banking.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "current_accounts")
@NoArgsConstructor
public class CurrentAccount extends BankAccount {
    private double overdraftLimit;
}
