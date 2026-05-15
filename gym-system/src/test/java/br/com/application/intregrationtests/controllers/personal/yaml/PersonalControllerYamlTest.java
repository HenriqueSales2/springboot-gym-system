package br.com.application.intregrationtests.controllers.personal.yaml;

import br.com.application.config.TestConfigs;
import br.com.application.intregrationtests.controllers.person.yaml.mapper.YAMLMapper;
import br.com.application.intregrationtests.dto.personal.PersonalDTO;
import br.com.application.intregrationtests.dto.personal.wrappers.xml.PagedModelPersonal;
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

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonalControllerYamlTest  extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static YAMLMapper mapper;
    private static PersonalDTO personalDTO;

    @BeforeAll
    static void setUp() {

        mapper = new YAMLMapper();
        personalDTO = new PersonalDTO();

    }

    @Test
    @Order(1)
    void createPersonalTestYaml() throws JsonProcessingException {

        mockPersonal();

        specification = new RequestSpecBuilder()
                .addHeaders(Map.of(TestConfigs.HEADER_PARAM_ORIGIN,
                        TestConfigs.ORIGIN_EXAMPLE))
                .setBasePath("/api/personal/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilters(List.of(new RequestLoggingFilter(LogDetail.ALL), new ResponseLoggingFilter(LogDetail.ALL)))
                .build();

        var content = given(specification)
                    .config(RestAssured.config().encoderConfig(encoderConfig().encodeContentTypeAs("application/yaml", ContentType.TEXT)))
                    .accept(MediaType.APPLICATION_YAML_VALUE)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                    .body(personalDTO, mapper)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                    .body()
                        .as(PersonalDTO.class, mapper);

        personalDTO = content;

        assertNotNull(content.getId());
        assertTrue(content.getId() > 0);

        assertEquals("Abigail", content.getFirstName());
        assertEquals("Roberts", content.getLastName());
        assertEquals("USA", content.getAddress());
        assertEquals("Female", content.getGender());
        assertFalse(content.isPersonal());

    }

    @Test
    @Order(2)
    void updatePersonalTestYaml() throws JsonProcessingException {

        personalDTO.setLastName("Marston");

        var content = given(specification)
                    .config(RestAssured.config().encoderConfig(encoderConfig().encodeContentTypeAs("application/yaml", ContentType.TEXT)))
                    .accept(MediaType.APPLICATION_YAML_VALUE)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                    .body(personalDTO, mapper)
                .when()
                    .put()
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                    .body()
                        .as(PersonalDTO.class, mapper);
        
        personalDTO = content;

        assertNotNull(content.getId());
        assertTrue(content.getId() > 0);


        assertEquals("Abigail", content.getFirstName());
        assertEquals("Marston", content.getLastName());
        assertEquals("USA", content.getAddress());
        assertEquals("Female", content.getGender());
        assertFalse(content.isPersonal());

    }

    @Test
    @Order(3)
    void findByIdPersonalTestYaml() throws JsonProcessingException {

        var content = given(specification)
                    .config(RestAssured.config().encoderConfig(encoderConfig().encodeContentTypeAs("application/yaml", ContentType.TEXT)))
                    .accept(MediaType.APPLICATION_YAML_VALUE)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                .pathParam("id", personalDTO.getId())
                .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                    .body()
                        .as(PersonalDTO.class, mapper);

        
        personalDTO = content;

        assertNotNull(content.getId());
        assertTrue(content.getId() > 0);

        assertEquals("Abigail", content.getFirstName());
        assertEquals("Marston", content.getLastName());
        assertEquals("USA", content.getAddress());
        assertEquals("Female", content.getGender());
        assertFalse(content.isPersonal());

    }

    @Test
    @Order(4)
    void deletePersonalTestYaml() throws JsonProcessingException {

        given(specification)
                    .pathParam("id", personalDTO.getId())
                .when()
                    .delete("{id}")
                .then()
                    .statusCode(204);

    }

    @Test
    @Order(5)
    void findAllTeachersTestYaml() throws JsonProcessingException {

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
                        .as(PagedModelPersonal.class, mapper);

        
        List<PersonalDTO> personals = response.getContent();

        PersonalDTO personalOne = personals.get(0);
        personalDTO = personalOne;

        assertNotNull(personalOne.getId());
        assertTrue(personalOne.getId() > 0);

        assertEquals("Amory", personalOne.getFirstName());
        assertEquals("Malloch", personalOne.getLastName());
        assertEquals("Room 927", personalOne.getAddress());
        assertEquals("Male", personalOne.getGender());
        assertFalse(personalOne.isPersonal());

        PersonalDTO personalFour = personals.get(4);
        personalDTO = personalFour;

        assertNotNull(personalFour.getId());
        assertTrue(personalFour.getId() > 0);

        assertEquals("Andreana", personalFour.getFirstName());
        assertEquals("Roofe", personalFour.getLastName());
        assertEquals("Suite 97", personalFour.getAddress());
        assertEquals("Female", personalFour.getGender());
        assertFalse(personalFour.isPersonal());

    }

    private void mockPersonal() {

        personalDTO.setFirstName("Abigail");
        personalDTO.setLastName("Roberts");
        personalDTO.setAddress("USA");
        personalDTO.setGender("Female");
        personalDTO.setPersonal(false);

    }
}
