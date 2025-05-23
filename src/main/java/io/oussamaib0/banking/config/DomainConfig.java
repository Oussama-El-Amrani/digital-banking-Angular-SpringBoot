package io.oussamaib0.banking.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("io.oussamaib0.banking.entities")
@EnableJpaRepositories("io.oussamaib0.banking.repositories")
@EnableTransactionManagement
public class DomainConfig {
}
