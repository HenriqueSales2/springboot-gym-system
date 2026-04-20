package br.com.application.intregrationtests.controllers.cors.withjson;

import br.com.application.config.TestConfigs;
import br.com.application.intregrationtests.dto.PersonDTO;
import br.com.application.intregrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) // vamos definir a porta que o spring boot vai utilizar para os testes
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerCorsTest extends AbstractIntegrationTest { // sem estender essa classe abstrata que criamos os testes falham

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static PersonDTO personDTO;

    @BeforeAll // para não ficar criando instâncias novas toda vez que um teste for executado
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // desabilita requisições para objetos desconhecidos
        personDTO = new PersonDTO();
    }

    @Test
    @Order(1) // vai ser o primeiro teste a ser executado
    void create() throws JsonProcessingException {
        mockPerson();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, // pegando os parâmetros setados no TestConfigs (esses parâmetros são que podemos setar no Postman na aba "Headers", esse por exemplo fica na parte de Key e é o Origin)
                        TestConfigs.ORIGIN_EXAMPLE) // pegando os parâmetros setados no TestConfigs (esses parâmetros são que podemos setar no Postman na aba "Headers", esse por exemplo fica na parte de Value e é https://example.com.br)
                .setBasePath("/api/person/v1") // aqui é a URLBASE, ou seja, o caminho que fazemos para criar um user
                .setPort(TestConfigs.SERVER_PORT) // aqui é a porta do servidor, nesse caso eu setei 8888, pois como aqui são testes se eu colocar 8080 pode conflitar com a aplicação base
                .addFilter(new RequestLoggingFilter(LogDetail.ALL)) // mostrar os destalhes das logs que estão indo na aplicação e investigar possíveis erros
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL)) // mostrar os destalhes das logs que vindo indo na aplicação e investigar possíveis erros
                .build();

        var content = given(specification) // armazenando todo esse conteúdo em uma variável
                .contentType(MediaType.APPLICATION_JSON_VALUE) // como se fosse o "application/json", usamos o método para evitar erros na hora da digitação (serve para aceitar JSON na hora de criar users)
                .body(personDTO)
                .when() // quando executar uma operação
                .post()// do tipo post
                .then() // então
                .statusCode(200)// eu espero a resposta de statusCode 200 OK (significa que deu tudo certo)
                .extract()
                .body() // pegar o conteúdo do body (corpo)
                .asString(); // transformar em String

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        personDTO = createdPerson;
        assertNotNull(createdPerson.getId()); // assegurar que o id não é nulo

        assertNotNull(createdPerson.getFirstName()); // assegurar que o "firstName" não é nulo
        assertNotNull(createdPerson.getLastName()); // assegurar que o "lastName" não é nulo
        assertNotNull(createdPerson.getAddress()); // assegurar que o "address" não é nulo
        assertNotNull(createdPerson.getGender()); // assegurar que o "gender" não é nulo

        assertTrue(createdPerson.getId() > 0); // assegurar que o id é maior que zero

        assertEquals("John", createdPerson.getFirstName()); // assegurar que o "firstName" de person que criamos é igual ao mockPerson que setei abaixo
        assertEquals("Doe", createdPerson.getLastName()); // assegurar que o "lastName" de person que criamos é igual ao mockPerson que setei abaixo
        assertEquals("New York City - New York - USA", createdPerson.getAddress()); // assegurar que o "address" de person que criamos é igual ao mockPerson que setei abaixo
        assertEquals("Male", createdPerson.getGender()); // // assegurar que o "gender" de person que criamos é igual ao mockPerson que setei abaixo
        assertTrue(createdPerson.getEnabled()); // verificar se o mock de uma pessoa está habilitado
    }

    @Test
    @Order(2) // vai ser o segundo teste a ser executado
    void createWithWrongOrigin() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, // pegando os parâmetros setados no TestConfigs (esses parâmetros são que podemos setar no Postman na aba "Headers", esse por exemplo fica na parte de Key e é o Origin)
                        TestConfigs.ORIGIN_ERROR) // pegando a URL que coloquei propositalmente errada
                .setBasePath("/api/person/v1") // aqui é a URLBASE, ou seja, o caminho que fazemos para criar um user
                .setPort(TestConfigs.SERVER_PORT) // aqui é a porta do servidor, nesse caso eu setei 8888, pois como aqui são testes se eu colocar 8080 pode conflitar com a aplicação base
                .addFilter(new RequestLoggingFilter(LogDetail.ALL)) // mostrar os detalhes das logs que estão indo na aplicação e investigar possíveis erros
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL)) // mostrar os detalhes das logs que vindo indo na aplicação e investigar possíveis erros
                .build();

        var content = given(specification) // armazenando todo esse conteúdo em uma variável
                .contentType(MediaType.APPLICATION_JSON_VALUE) // como se fosse o "application/json", usamos o método para evitar erros na hora da digitação (serve para aceitar JSON na hora de criar users)
                .body(personDTO)
                .when() // quando executar uma operação
                .post()// do tipo post
                .then() // então
                .statusCode(403)// eu espero a resposta de statusCode 403 Forbidden (significa que deu um erro)
                .extract()
                .body() // pegar o conteúdo do body (corpo)
                .asString(); // transformar em String

        assertEquals("Invalid CORS request", content); // esperamos essa mensagem, pois esse Mock é para simular um erro de "Invalid CORS request"
    }


    @Test
    @Order(3) // vai ser o terceiro teste a ser executado
    void findById() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, // pegando os parâmetros setados no TestConfigs (esses parâmetros são que podemos setar no Postman na aba "Headers", esse por exemplo fica na parte de Key e é o Origin)
                        TestConfigs.ORIGIN_LOCAL) // verificando diferentes Headers para verificar se está tudo funcionando
                .setBasePath("/api/person/v1") // aqui é a URLBASE, ou seja, o caminho que fazemos para criar um user
                .setPort(TestConfigs.SERVER_PORT) // aqui é a porta do servidor, nesse caso eu setei 8888, pois como aqui são testes se eu colocar 8080 pode conflitar com a aplicação base
                .addFilter(new RequestLoggingFilter(LogDetail.ALL)) // mostrar os destalhes das logs que estão indo na aplicação e investigar possíveis erros
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL)) // mostrar os destalhes das logs que vindo indo na aplicação e investigar possíveis erros
                .build();

        var content = given(specification) // armazenando todo esse conteúdo em uma variável
                .contentType(MediaType.APPLICATION_JSON_VALUE) // como se fosse o "application/json", usamos o método para evitar erros na hora da digitação (serve para aceitar JSON na hora de criar users)
                .pathParam("id", personDTO.getId()) // dessa vez passamos o parâmetro através do pathParam, ou seja, direto da URL, nesse caso eu coloco o nome do parâmetro e depois pego id da classe PersonDTO
                .when() // quando executar uma operação
                .get("{id}")// do tipo get e passando o "id" como parâmetro, faço a mesma coisa no Controller quando chamo a Annotation "GetMapping", porém a única diferença é que aqui são testes
                .then() // então
                .statusCode(200)// eu espero a resposta de statusCode 200 OK (significa que deu tudo certo)
                .extract()
                .body() // pegar o conteúdo do body (corpo)
                .asString(); // transformar em String

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        personDTO = createdPerson;
        assertNotNull(createdPerson.getId()); // assegurar que o id não é nulo

        assertNotNull(createdPerson.getFirstName()); // assegurar que o "firstName" não é nulo
        assertNotNull(createdPerson.getLastName()); // assegurar que o "lastName" não é nulo
        assertNotNull(createdPerson.getAddress()); // assegurar que o "address" não é nulo
        assertNotNull(createdPerson.getGender()); // assegurar que o "gender" não é nulo

        assertTrue(createdPerson.getId() > 0); // assegurar que o id é maior que zero

        assertEquals("John", createdPerson.getFirstName()); // assegurar que o "firstName" de person que criamos é igual ao mockPerson que setei abaixo
        assertEquals("Doe", createdPerson.getLastName()); // assegurar que o "lastName" de person que criamos é igual ao mockPerson que setei abaixo
        assertEquals("New York City - New York - USA", createdPerson.getAddress()); // assegurar que o "address" de person que criamos é igual ao mockPerson que setei abaixo
        assertEquals("Male", createdPerson.getGender()); // // assegurar que o "gender" de person que criamos é igual ao mockPerson que setei abaixo
        assertTrue(createdPerson.getEnabled()); // verificar se o mock de uma pessoa está habilitado
         }

    @Test
    @Order(4) // vai ser o quarto teste a ser executado
    void findByIdWithWrongOrigin() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, // pegando os parâmetros setados no TestConfigs (esses parâmetros são que podemos setar no Postman na aba "Headers", esse por exemplo fica na parte de Key e é o Origin)
                        TestConfigs.ORIGIN_ERROR) //  pegando a URL que coloquei propositalmente errada
                .setBasePath("/api/person/v1") // aqui é a URLBASE, ou seja, o caminho que fazemos para criar um user
                .setPort(TestConfigs.SERVER_PORT) // aqui é a porta do servidor, nesse caso eu setei 8888, pois como aqui são testes se eu colocar 8080 pode conflitar com a aplicação base
                .addFilter(new RequestLoggingFilter(LogDetail.ALL)) // mostrar os destalhes das logs que estão indo na aplicação e investigar possíveis erros
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL)) // mostrar os destalhes das logs que vindo indo na aplicação e investigar possíveis erros
                .build();

        var content = given(specification) // armazenando todo esse conteúdo em uma variável
                .contentType(MediaType.APPLICATION_JSON_VALUE) // como se fosse o "application/json", usamos o método para evitar erros na hora da digitação (serve para aceitar JSON na hora de criar users)
                .pathParam("id", personDTO.getId()) // dessa vez passamos o parâmetro através do pathParam, ou seja, direto da URL, nesse caso eu coloco o nome do parâmetro e depois pego id da classe PersonDTO
                .when() // quando executar uma operação
                .get("{id}")// do tipo get e passando o "id" como parâmetro, faço a mesma coisa no Controller quando chamo a Annotation "GetMapping", porém a única diferença é que aqui são testes
                .then() // então
                .statusCode(403)// eu espero a resposta de statusCode 403 Forbidden (significa que deu um erro)
                .extract()
                .body() // pegar o conteúdo do body (corpo)
                .asString(); // transformar em String


        assertEquals("Invalid CORS request", content); // esperamos essa mensagem, pois esse Mock é para simular um erro de "Invalid CORS request"
    }

//    @Test
//    void update() {
//    }

//    @Test
//    void delete() {
//    }

//    @Test
//    void findAll() {
//    }

    private void mockPerson() {
        personDTO.setFirstName("John");
        personDTO.setLastName("Doe");
        personDTO.setAddress("New York City - New York - USA");
        personDTO.setGender("Male");
        personDTO.setEnabled(true);

    }
}