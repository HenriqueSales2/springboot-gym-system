package br.com.application.intregrationtests.controllers.person.yaml;

import br.com.application.config.TestConfigs;
import br.com.application.intregrationtests.controllers.person.yaml.mapper.YAMLMapper;
import br.com.application.intregrationtests.dto.PersonDTO;
import br.com.application.intregrationtests.dto.wrappers.xml.PagedModelPerson;
import br.com.application.intregrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerYamlTest extends AbstractIntegrationTest {

    public static RequestSpecification specification;
    public static YAMLMapper mapper;
    public static PersonDTO personDTO;


    @BeforeAll
    static void setUp() throws JsonProcessingException {

        mapper = new YAMLMapper();
        personDTO = new PersonDTO();
    }

    @Test
    @Order(1)
    void createTestYAML() throws JsonProcessingException {

        mockPersonYAML();
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN ,
                        TestConfigs.ORIGIN_LOCAL
                )
                .setBasePath("/api/person/v1")
                .setPort(TestConfigs.SERVER_PORT) // trocar aqui talvez
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                    .config(RestAssured.config().encoderConfig(encoderConfig().encodeContentTypeAs("application/yaml", ContentType.TEXT)))
                    .accept(MediaType.APPLICATION_YAML_VALUE)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                    .body(personDTO, mapper) // serializando
                .when()
                    .post()
                .then()
                    .statusCode(200)
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
    void updateTestYAML() throws JsonProcessingException {

        personDTO.setFirstName("Jim");
        personDTO.setLastName("Milton");

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN,
                        TestConfigs.ORIGIN_LOCAL
                )
                .setBasePath("/api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

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
    void findByIdTestYAML() throws JsonProcessingException {

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
    void disablePersonTestYAML() throws JsonProcessingException {

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
    void deleteTestYAML() throws JsonProcessingException {

        var content = given(specification)
                    .pathParam("id", personDTO.getId())
                .when()
                    .delete("{id}")
                .then()
                    .statusCode(204);
    }

    @Test
    @Order(6)
    void findAllTestYAML() throws JsonProcessingException {

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
                        .as(PagedModelPerson.class, mapper); // passando um Array de PersonDTO

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

    private void mockPersonYAML() {

        personDTO.setFirstName("John");
        personDTO.setLastName("Marston");
        personDTO.setAddress("North - EUA");
        personDTO.setGender("Male");
        personDTO.setEnabled(true);
    }

}
