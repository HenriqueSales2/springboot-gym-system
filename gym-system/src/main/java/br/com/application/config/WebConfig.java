package br.com.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

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
