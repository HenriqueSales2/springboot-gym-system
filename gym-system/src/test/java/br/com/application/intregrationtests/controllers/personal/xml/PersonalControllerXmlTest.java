package br.com.application.intregrationtests.controllers.personal.xml;

import br.com.application.config.TestConfigs;
import br.com.application.intregrationtests.dto.personal.PersonalDTO;
import br.com.application.intregrationtests.dto.personal.wrappers.xml.PagedModelPersonal;
import br.com.application.intregrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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
public class PersonalControllerXmlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static XmlMapper mapper;
    private static PersonalDTO personalDTO;

    @BeforeAll
    static void setUp() {

        mapper = new XmlMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        personalDTO = new PersonalDTO();

    }

    @Test
    @Order(1)
    void createPersonalTestXml() throws JsonProcessingException {

        mockPersonal();

        specification = new RequestSpecBuilder()
                .addHeaders(Map.of(TestConfigs.HEADER_PARAM_ORIGIN,
                        TestConfigs.ORIGIN_EXAMPLE))
                .setBasePath("/api/personal/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilters(List.of(new RequestLoggingFilter(LogDetail.ALL), new ResponseLoggingFilter(LogDetail.ALL)))
                .build();

        var content = given(specification)
                    .accept(MediaType.APPLICATION_XML_VALUE)
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                    .body(personalDTO)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                    .body()
                        .asString();

        PersonalDTO createdPersonal = mapper.readValue(content, PersonalDTO.class);
        personalDTO = createdPersonal;

        assertNotNull(createdPersonal.getId());
        assertTrue(createdPersonal.getId() > 0);

        assertEquals("Micah", createdPersonal.getFirstName());
        assertEquals("Bell", createdPersonal.getLastName());
        assertEquals("Ohio - USA", createdPersonal.getAddress());
        assertEquals("Male", createdPersonal.getGender());
        assertTrue(createdPersonal.isPersonal());

    }

    @Test
    @Order(2)
    void updatePersonalTestXml() throws JsonProcessingException {

        personalDTO.setLastName("O'Driscoll");

        var content = given(specification)
                    .accept(MediaType.APPLICATION_XML_VALUE)
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                    .body(personalDTO)
                .when()
                    .put()
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                    .body()
                        .asString();

        PersonalDTO createdPersonal = mapper.readValue(content, PersonalDTO.class);
        personalDTO = createdPersonal;

        assertNotNull(createdPersonal.getId());
        assertTrue(createdPersonal.getId() > 0);


        assertEquals("Micah", createdPersonal.getFirstName());
        assertEquals("O'Driscoll", createdPersonal.getLastName());
        assertEquals("Ohio - USA", createdPersonal.getAddress());
        assertEquals("Male", createdPersonal.getGender());
        assertTrue(createdPersonal.isPersonal());

    }

    @Test
    @Order(3)
    void findByIdPersonalTestXml() throws JsonProcessingException {

        var content = given(specification)
                    .accept(MediaType.APPLICATION_XML_VALUE)
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                    .pathParam("id", personalDTO.getId())
                .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                    .body()
                        .asString();

        PersonalDTO createdPersonal = mapper.readValue(content, PersonalDTO.class);
        personalDTO = createdPersonal;

        assertNotNull(createdPersonal.getId());
        assertTrue(createdPersonal.getId() > 0);

        assertEquals("Micah", createdPersonal.getFirstName());
        assertEquals("O'Driscoll", createdPersonal.getLastName());
        assertEquals("Ohio - USA", createdPersonal.getAddress());
        assertEquals("Male", createdPersonal.getGender());
        assertTrue(createdPersonal.isPersonal());

    }

    @Test
    @Order(4)
    void deletePersonalTestXml() throws JsonProcessingException {

        given(specification)
                    .pathParam("id", personalDTO.getId())
                .when()
                    .delete("{id}")
                .then()
                    .statusCode(204);

    }

    @Test
    @Order(5)
    void findAllTeachersTestXml() throws JsonProcessingException {

        var content = given(specification)
                    .accept(MediaType.APPLICATION_XML_VALUE)
                    .queryParams("page", 3, "size", 12, "direction", "asc")
                .when()
                    .get()
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                    .body()
                        .asString();

        //WrapperPersonDTO wrapper = mapper.readValue(content, WrapperPersonDTO.class);
        PagedModelPersonal wrapper = mapper.readValue(content, PagedModelPersonal.class);
        List<PersonalDTO> personals = wrapper.getContent();

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

        personalDTO.setFirstName("Micah");
        personalDTO.setLastName("Bell");
        personalDTO.setAddress("Ohio - USA");
        personalDTO.setGender("Male");
        personalDTO.setPersonal(true);

    }
}
