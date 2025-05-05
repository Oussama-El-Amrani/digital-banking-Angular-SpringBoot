package io.oussamaib0.banking.repositories;

import io.oussamaib0.banking.entities.CurrentAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CurrentAccountRepository extends JpaRepository<CurrentAccount, UUID> {
}
