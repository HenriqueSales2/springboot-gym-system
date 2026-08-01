package br.com.application.intregrationtests.controllers.person.yaml;

import br.com.application.config.TestConfigs;
import br.com.application.intregrationtests.controllers.person.yaml.mapper.YAMLMapper;
import br.com.application.intregrationtests.dto.person.PersonDTO;
import br.com.application.intregrationtests.dto.person.wrappers.xml.PagedModelPerson;
import br.com.application.intregrationtests.dto.security.AccountCredentialsDTO;
import br.com.application.intregrationtests.dto.security.TokenDTO;
import br.com.application.intregrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerYamlTest extends AbstractIntegrationTest {

    public static RequestSpecification specification;
    public static YAMLMapper mapper;
    public static PersonDTO personDTO;
    public static TokenDTO tokenDTO;

    @BeforeAll
    static void setUp() {
        mapper = new YAMLMapper();
        personDTO = new PersonDTO();
        tokenDTO = new TokenDTO();
    }

    @Test
    @Order(0)
    void sigIn() {
        AccountCredentialsDTO credentials = new AccountCredentialsDTO("john", "admin123");

        tokenDTO = given()
                    .config(RestAssured.config().encoderConfig(encoderConfig().encodeContentTypeAs("application/yaml", ContentType.TEXT)))
                    .basePath("auth/signin")
                    .port(TestConfigs.SERVER_PORT)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                    .accept(MediaType.APPLICATION_YAML_VALUE)
                    .body(credentials, mapper)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                    .body()
                        .as(TokenDTO.class, mapper);

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }

    @Test
    @Order(1)
    void createTestYAML() {
        mockPersonYAML();
        specification = new RequestSpecBuilder()
                .addHeaders(Map.of(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL))
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, TestConfigs.BEARER_PREFIX + tokenDTO.getAccessToken())
                .setBasePath(TestConfigs.BASEPATH_PARAM)
                .setPort(TestConfigs.SERVER_PORT)
                .addFilters(List.of(new RequestLoggingFilter(LogDetail.ALL), new ResponseLoggingFilter(LogDetail.ALL)))
                .build();

        var content = given(specification)
                    .config(RestAssured.config().encoderConfig(encoderConfig().encodeContentTypeAs("application/yaml", ContentType.TEXT)))
                    .accept(MediaType.APPLICATION_YAML_VALUE)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                    .body(personDTO, mapper) // serializando
                .when()
                    .post()
                .then()
                    .statusCode(201)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                    .body()
                        .as(PersonDTO.class, mapper); // desserializando

        personDTO = content;

        assertNotNull(content.getId());
        assertTrue(content.getId() > 0);

        personDTO.setFirstName("John");
        personDTO.setLastName("Marston");
        personDTO.setAddress("North - EUA");
        personDTO.setGender("Male");
        personDTO.setEnabled(true);
    }

    @Test
    @Order(2)
    void updateTestYAML() {
        personDTO.setFirstName("Jim");
        personDTO.setLastName("Milton");

        var content = given(specification)
                    .config(RestAssured.config().encoderConfig(encoderConfig().encodeContentTypeAs("application/yaml", ContentType.TEXT)))
                    .accept(MediaType.APPLICATION_YAML_VALUE)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                    .body(personDTO, mapper)
                .when()
                    .put()
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                    .body()
                        .as(PersonDTO.class, mapper);

        personDTO = content;

        assertNotNull(content.getId());
        assertTrue(content.getId() > 0);

        assertEquals("Jim", content.getFirstName());
        assertEquals("Milton", content.getLastName());
        assertEquals("North - EUA", content.getAddress());
        assertEquals("Male", content.getGender());
        assertTrue(content.getEnabled());
    }

    @Test
    @Order(3)
    void findByIdTestYAML() {
        var content = given(specification)
                    .config(RestAssured.config().encoderConfig(encoderConfig().encodeContentTypeAs("application/yaml", ContentType.TEXT)))
                    .accept(MediaType.APPLICATION_YAML_VALUE)
                    .pathParam("id", personDTO.getId())
                .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                    .body()
                        .as(PersonDTO.class, mapper);

        personDTO = content;

        assertNotNull(content.getId());
        assertTrue(content.getId() > 0);

        assertEquals("Jim", content.getFirstName());
        assertEquals("Milton", content.getLastName());
        assertEquals("North - EUA", content.getAddress());
        assertEquals("Male", content.getGender());
        assertTrue(content.getEnabled());
    }

    @Test
    @Order(4)
    void disablePersonTestYAML() {
        var content = given(specification)
                    .config(RestAssured.config().encoderConfig(encoderConfig().encodeContentTypeAs("application/yaml", ContentType.TEXT)))
                    .accept(MediaType.APPLICATION_YAML_VALUE)
                    .pathParam("id", personDTO.getId())
                .when()
                    .patch("{id}")
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                    .body()
                        .as(PersonDTO.class, mapper);

        personDTO = content;

        assertNotNull(content.getId());
        assertTrue(content.getId() > 0);

        assertEquals("Jim", content.getFirstName());
        assertEquals("Milton", content.getLastName());
        assertEquals("North - EUA", content.getAddress());
        assertEquals("Male", content.getGender());
        assertFalse(content.getEnabled());
    }

    @Test
    @Order(5)
    void deleteTestYAML() {
        given(specification)
                    .pathParam("id", personDTO.getId())
                .when()
                    .delete("{id}")
                .then()
                    .statusCode(204);
    }

    @Test
    @Order(6)
    void findAllTestYAML() {
        var response = given(specification)
                    .accept(MediaType.APPLICATION_YAML_VALUE)
                    .queryParams("page", 3, "size", 12, "direction", "asc")
                .when()
                    .get()
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                    .body()
                        .as(PagedModelPerson.class, mapper);

        List<PersonDTO> people = response.getContent();

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
    void findPeopleByNameTest() {
        var content = given(specification)
                    .accept(MediaType.APPLICATION_YAML_VALUE)
                    .pathParam("firstName", "and")
                    .queryParams("page", 0, "size", 12, "direction", "asc")
                .when()
                    .get("findPeopleByName/{firstName}")
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                    .body()
                        .as(PagedModelPerson.class, mapper);

        List<PersonDTO> people = content.getContent();

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

    private void mockPersonYAML() {
        personDTO.setFirstName("John");
        personDTO.setLastName("Marston");
        personDTO.setAddress("North - EUA");
        personDTO.setGender("Male");
        personDTO.setEnabled(true);
    }
}