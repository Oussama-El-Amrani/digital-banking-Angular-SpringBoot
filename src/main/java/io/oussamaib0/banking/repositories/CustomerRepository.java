package io.oussamaib0.banking.repositories;

import io.oussamaib0.banking.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);

    @Query("""
        SELECT c
        FROM Customer c LEFT JOIN FETCH c.bankAccounts
        """)
    List<Customer> findAllWithAccounts();

    @Query("""
        SELECT c
        FROM Customer c LEFT JOIN FETCH c.bankAccounts
        WHERE c.id= :id
        """)
    Optional<Customer> findByIdWithAccounts(Long id);
}
