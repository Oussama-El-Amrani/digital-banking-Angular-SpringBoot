package io.oussamaib0.banking.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "bank_accounts")
@NoArgsConstructor
@Data
public abstract class BankAccount {
    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;
    private String accountNumber;
    private String accountType;
    private String currency;
    private Double balance;

    @ManyToOne
    private Customer customer;
    @OneToMany(mappedBy = "bankAccount")
    private List<AccountOperation> transactions;
}
