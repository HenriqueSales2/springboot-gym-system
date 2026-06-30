package br.com.application.intregrationtests.controllers.person.json;

import br.com.application.config.TestConfigs;
import br.com.application.intregrationtests.dto.person.PersonDTO;
import br.com.application.intregrationtests.dto.person.wrappers.json.WrapperPersonDTO;
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

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerJsonTest extends AbstractIntegrationTest {

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
    void createTest() throws JsonProcessingException {
        mockPerson();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN,
                        TestConfigs.ORIGIN_EXAMPLE)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, TestConfigs.BEARER_PREFIX + tokenDTO.getAccessToken())
                .setBasePath(TestConfigs.BASEPATH_PARAM)
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(personDTO)
                .when()
                    .post()
                .then()
                    .statusCode(201)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                    .body()
                        .asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        personDTO = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Arthur", createdPerson.getFirstName());
        assertEquals("Morgan", createdPerson.getLastName());
        assertEquals("Indiana - USA", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(2)
    void updateTest() throws JsonProcessingException {
        personDTO.setLastName("Callahan");

        var content = given(specification)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(personDTO)
                .when()
                    .put()
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                    .body()
                        .asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        personDTO = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Arthur", createdPerson.getFirstName());
        assertEquals("Callahan", createdPerson.getLastName());
        assertEquals("Indiana - USA", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(3)
    void findByIdTest() throws JsonProcessingException {
        var content = given(specification)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .pathParam("id", personDTO.getId())
                .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                    .body()
                        .asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        personDTO = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Arthur", createdPerson.getFirstName());
        assertEquals("Callahan", createdPerson.getLastName());
        assertEquals("Indiana - USA", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(4)
    void disablePersonTest() throws JsonProcessingException {
        var content = given(specification)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .pathParam("id", personDTO.getId())
                .when()
                    .patch("{id}")
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                    .body()
                        .asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        personDTO = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Arthur", createdPerson.getFirstName());
        assertEquals("Callahan", createdPerson.getLastName());
        assertEquals("Indiana - USA", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertFalse(createdPerson.getEnabled());
    }

    @Test
    @Order(5)
    void deleteTest() {
        given(specification)
                .pathParam("id", personDTO.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(6)
    void findAllTest() throws JsonProcessingException {
        var content = given(specification)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .queryParams("page", 3, "size", 12, "direction", "asc")
                .when()
                    .get()
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                    .body()
                        .asString();

        WrapperPersonDTO wrapper = objectMapper.readValue(content, WrapperPersonDTO.class);
        List<PersonDTO> people = wrapper.getEmbedded().getPeople();

        PersonDTO personOne = people.get(0);
        personDTO = personOne;

        assertNotNull(personOne.getId());
        assertTrue(personOne.getId() > 0);

        assertEquals("Aluino", personOne.getFirstName());
        assertEquals("Ollive", personOne.getLastName());
        assertEquals("Apt 239", personOne.getAddress());
        assertEquals("Male", personOne.getGender());
        assertTrue(personOne.getEnabled());

        PersonDTO personFour = people.get(4);
        personDTO = personFour;

        assertNotNull(personOne.getId());
        assertTrue(personOne.getId() > 0);

        assertEquals("Alyssa", personFour.getFirstName());
        assertEquals("BURWIN", personFour.getLastName());
        assertEquals("Apt 551", personFour.getAddress());
        assertEquals("Female", personFour.getGender());
        assertFalse(personFour.getEnabled());
    }

    @Test
    @Order(7)
    void findPeopleByNameTest() throws JsonProcessingException {
        var content = given(specification)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .pathParam("firstName", "and")
                    .queryParams("page", 0, "size", 12, "direction", "asc")
                .when()
                    .get("findPeopleByName/{firstName}")
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                    .body()
                        .asString();

        WrapperPersonDTO wrapper = objectMapper.readValue(content, WrapperPersonDTO.class);
        List<PersonDTO> people = wrapper.getEmbedded().getPeople();


        PersonDTO personOne = people.get(0);
        personDTO = personOne;

        assertNotNull(personOne.getId());
        assertTrue(personOne.getId() > 0);

        assertEquals("Alejandrina", personOne.getFirstName());
        assertEquals("Turbayne", personOne.getLastName());
        assertEquals("Room 1511", personOne.getAddress());
        assertEquals("Female", personOne.getGender());
        assertTrue(personOne.getEnabled());

        PersonDTO personFour = people.get(4);
        personDTO = personFour;

        assertNotNull(personFour.getId());
        assertTrue(personFour.getId() > 0);

        assertEquals("Andie", personFour.getFirstName());
        assertEquals("Gawler", personFour.getLastName());
        assertEquals("Apt 234", personFour.getAddress());
        assertEquals("Female", personFour.getGender());
        assertFalse(personFour.getEnabled());
    }

    private void mockPerson() {
        personDTO.setFirstName("Arthur");
        personDTO.setLastName("Morgan");
        personDTO.setAddress("Indiana - USA");
        personDTO.setGender("Male");
        personDTO.setEnabled(true);
    }
}