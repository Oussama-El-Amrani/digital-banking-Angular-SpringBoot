package io.oussamaib0.banking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI bankingAppOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Banking Application API")
                .description("REST API for banking operations")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Oussama El-Amrani")
                    .email("elamranioussama01@gmail.com")));
    }
}
