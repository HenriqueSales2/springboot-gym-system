package br.com.application.intregrationtests.controllers.person.json;

import br.com.application.config.TestConfigs;
import br.com.application.intregrationtests.dto.person.PersonDTO;
import br.com.application.intregrationtests.dto.security.AccountCredentialsDTO;
import br.com.application.intregrationtests.dto.security.TokenDTO;
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

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerCorsTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static PersonDTO personDTO;
    private static TokenDTO tokenDTO;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        personDTO = new PersonDTO();
        tokenDTO = new TokenDTO();
    }

    @Test
    @Order(0)
    void sigIn() {
        AccountCredentialsDTO credentials = new AccountCredentialsDTO("john", "admin123");

        tokenDTO = given()
                    .basePath("auth/signin")
                    .port(TestConfigs.SERVER_PORT)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(credentials)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                    .body()
                        .as(TokenDTO.class);

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }

    @Test
    @Order(1)
    void create() throws JsonProcessingException {
        mockPerson();

        specification = new RequestSpecBuilder()
                .addHeaders(Map.of(TestConfigs.HEADER_PARAM_ORIGIN,
                        TestConfigs.ORIGIN_EXAMPLE))
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, TestConfigs.BEARER_PREFIX + tokenDTO.getAccessToken())
                .setBasePath(TestConfigs.BASEPATH_PARAM)
                .setPort(TestConfigs.SERVER_PORT)
                .addFilters(List.of(new RequestLoggingFilter(LogDetail.ALL), new ResponseLoggingFilter(LogDetail.ALL)))
                .build();

        var content = given(specification)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(personDTO)
                .when()
                    .post()
                .then()
                    .statusCode(201)
                .extract()
                    .body()
                        .asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        personDTO = createdPerson;
        assertNotNull(createdPerson.getId());

        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());

        assertTrue(createdPerson.getId() > 0);

        assertEquals("Mary", createdPerson.getFirstName());
        assertEquals("Doe", createdPerson.getLastName());
        assertEquals("New York - USA", createdPerson.getAddress());
        assertEquals("Female", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(2)
    void createWithWrongOrigin() {
        specification = new RequestSpecBuilder()
                .addHeaders(Map.of(TestConfigs.HEADER_PARAM_ORIGIN,
                        TestConfigs.ORIGIN_ERROR))
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, TestConfigs.BEARER_PREFIX + tokenDTO.getAccessToken())
                .setBasePath(TestConfigs.BASEPATH_PARAM)
                .setPort(TestConfigs.SERVER_PORT)
                .addFilters(List.of(new RequestLoggingFilter(LogDetail.ALL), new ResponseLoggingFilter(LogDetail.ALL)))
                .build();

        var content = given(specification)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(personDTO)
                .when()
                    .post()
                .then()
                    .statusCode(403)
                .extract()
                    .body()
                        .asString();

        assertEquals("Invalid CORS request", content);
    }

    @Test
    @Order(3)
    void findById() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .addHeaders(Map.of(TestConfigs.HEADER_PARAM_ORIGIN,
                        TestConfigs.ORIGIN_EXAMPLE))
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, TestConfigs.BEARER_PREFIX + tokenDTO.getAccessToken())
                .setBasePath(TestConfigs.BASEPATH_PARAM)
                .setPort(TestConfigs.SERVER_PORT)
                .addFilters(List.of(new RequestLoggingFilter(LogDetail.ALL), new ResponseLoggingFilter(LogDetail.ALL)))
                .build();

        var content = given(specification)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .pathParam("id", personDTO.getId())
                .when()
                    .get("{id}")
                .then() // então
                    .statusCode(200)
                .extract()
                    .body()
                        .asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        personDTO = createdPerson;
        assertNotNull(createdPerson.getId());

        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());

        assertTrue(createdPerson.getId() > 0);

        assertEquals("Mary", createdPerson.getFirstName());
        assertEquals("Doe", createdPerson.getLastName());
        assertEquals("New York - USA", createdPerson.getAddress());
        assertEquals("Female", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(4)
    void findByIdWithWrongOrigin() {
        specification = new RequestSpecBuilder()
                .addHeaders(Map.of(TestConfigs.HEADER_PARAM_ORIGIN,
                        TestConfigs.ORIGIN_ERROR))
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, TestConfigs.BEARER_PREFIX + tokenDTO.getAccessToken())
                .setBasePath(TestConfigs.BASEPATH_PARAM)
                .setPort(TestConfigs.SERVER_PORT)
                .addFilters(List.of(new RequestLoggingFilter(LogDetail.ALL), new ResponseLoggingFilter(LogDetail.ALL)))
                .build();

        var content = given(specification)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .pathParam("id", personDTO.getId())
                .when()
                    .get("{id}")
                .then()
                    .statusCode(403)
                .extract()
                    .body()
                        .asString();

        assertEquals("Invalid CORS request", content);
    }

    private void mockPerson() {
        personDTO.setFirstName("Mary");
        personDTO.setLastName("Doe");
        personDTO.setAddress("New York - USA");
        personDTO.setGender("Female");
        personDTO.setEnabled(true);
    }
}