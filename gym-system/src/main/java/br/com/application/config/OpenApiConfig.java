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
                        .title("Gym Management API")
                        .version("v1")
                        .description("API RESTful para gerenciamento de academias, permitindo o controle de alunos, treinadores e treinos de forma segura e eficiente.")
                        .termsOfService("https://github.com/HenriqueSales2")
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
