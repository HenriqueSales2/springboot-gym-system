package br.com.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${cors.originPatterns:default}") // spring vai setar essa propriedade através do application.yml na váriavel abaixo
    private String corsOriginPatterns = "";

    @Override
    public void addCorsMappings(CorsRegistry registry) { // habilitando CORS na aplicação através deste método
        var allowedOrigins = corsOriginPatterns.split(","); // isso vai gerar uma lista com as URLS que setei no application.yml, e vai splitar (cortar as virgulas e pegar só as URLS)
        registry.addMapping("/**") // todas as requisições vão ser filtradas por CORS
                .allowedOrigins(allowedOrigins)
                //.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // caso queira filtrar os métodos que deseja permitir
                .allowedMethods("*") // eu vou permitir todos, usando o "*"
                .allowCredentials(true);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        //WebMvcConfigurer.super.configureContentNegotiation(configurer);

        // Via EXTENSION. http://localhost:8080/person/3.xml ou http://localhost:8080/person/3.json foi Depreciado no Spring Boot 2.6

        // Via QUERY PARAM http://localhost:8080/person/3?mediaType=xml ou http://localhost:8080/person/3?mediaType=json
        /*
        configurer.favorParameter(true)
                .parameterName("mediaType")
                .ignoreAcceptHeader(true) // ignora o que tem na linha
                .useRegisteredExtensionsOnly(false)
                .defaultContentType(MediaType.APPLICATION_JSON)
                .mediaType("json", MediaType.APPLICATION_JSON)
                .mediaType("xml", MediaType.APPLICATION_XML);
                */

        // Via HEADER PARAM ( KEY: Accept | Value: application/xml
        configurer.favorParameter(false) // não há necessidade de colocar parâmetros na URL
                .ignoreAcceptHeader(false) // não pode ignorar o que está escrito na linha
                .useRegisteredExtensionsOnly(false)
                .defaultContentType(MediaType.APPLICATION_JSON)
                .mediaType("json", MediaType.APPLICATION_JSON) // permite fazer os métodos HTTPs pelo Header Param e utilizar JSON como forma de consultar os dados
                .mediaType("xml", MediaType.APPLICATION_XML) // permite fazer os métodos HTTPs pelo Header Param e utilizar XML como forma de consultar os dados
                .mediaType("yaml", MediaType.APPLICATION_YAML); // permite fazer os métodos HTTPs pelo Header Param e utilizar YAML como forma de consultar os dados

    }


}
