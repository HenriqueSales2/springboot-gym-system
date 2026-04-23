package br.com.application.intregrationtests.controllers.person.withyaml.complexversion;
// ainda incompleto
import br.com.application.config.TestConfigs;
import br.com.application.intregrationtests.controllers.person.withyaml.complexversion.mapper.YAMLMapper;
import br.com.application.intregrationtests.dto.withyaml.PersonDTOYaml;
import br.com.application.intregrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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
    public static PersonDTOYaml personDTO;


    @BeforeAll
    static void setUp() throws JsonProcessingException {

        mapper = new YAMLMapper();
        personDTO = new PersonDTOYaml();
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
                        .as(PersonDTOYaml.class, mapper); // desserializando

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
                        .as(PersonDTOYaml.class, mapper);

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
                        .as(PersonDTOYaml.class, mapper);

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
                        .as(PersonDTOYaml.class, mapper);

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
                .when()
                    .get()
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                    .body()
                        .as(PersonDTOYaml[].class, mapper); // passando um Array de PersonDTOYaml

        List<PersonDTOYaml> people = Arrays.asList(response);

        PersonDTOYaml personOne = people.get(0);
        personDTO = personOne;

        assertNotNull(personOne.getId());
        assertTrue(personOne.getId() > 0);

        assertEquals("Lucas", personOne.getFirstName());
        assertEquals("Ferreira", personOne.getLastName());
        assertEquals("São Paulo - Brasil", personOne.getAddress());
        assertEquals("Male", personOne.getGender());
        assertTrue(personOne.getEnabled());

        PersonDTOYaml personFour = people.get(4);
        personDTO = personFour;

        assertNotNull(personOne.getId());
        assertTrue(personOne.getId() > 0);

        assertEquals("Gabriel", personFour.getFirstName());
        assertEquals("Martins", personFour.getLastName());
        assertEquals("Paraná - Brasil", personFour.getAddress());
        assertEquals("Male", personFour.getGender());
        assertTrue(personFour.getEnabled());

    }

    private void mockPersonYAML() {

        personDTO.setFirstName("John");
        personDTO.setLastName("Marston");
        personDTO.setAddress("North - EUA");
        personDTO.setGender("Male");
        personDTO.setEnabled(true);
    }

}
