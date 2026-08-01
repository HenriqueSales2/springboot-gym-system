package br.com.application.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Gym Training API")
                        .version("v1")
                        .description("""
    Gym Training API é uma API RESTful desenvolvida com Java 21, Spring Boot, Spring Data JPA, MySQL, OpenAPI/Swagger e JasperReports para o gerenciamento de exercícios de academia e geração de relatórios em PDF.
    
    A aplicação disponibiliza endpoints para operações CRUD, consultas de exercícios por grupo muscular, equipamentos e nível de dificuldade, além da exportação de relatórios profissionais utilizando JasperReports.

    O projeto segue uma arquitetura em camadas, aplicando boas práticas de desenvolvimento, documentação de APIs, persistência de dados e tratamento de exceções, servindo como demonstração de competências em desenvolvimento Back-End com Spring Boot.
    """)
                        .termsOfService("https://github.com/HenriqueSales2")
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}