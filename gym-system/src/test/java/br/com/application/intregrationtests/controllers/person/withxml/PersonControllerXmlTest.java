package br.com.application.intregrationtests.controllers.person.withxml;

import br.com.application.config.TestConfigs;
import br.com.application.intregrationtests.dto.withxml.PersonDTOXml;
import br.com.application.intregrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerXmlTest extends AbstractIntegrationTest {

    public static RequestSpecification specification;
    public static XmlMapper mapper;
    public static PersonDTOXml personDTO;

    @BeforeAll
    static void setUp() {

        mapper = new XmlMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        personDTO = new PersonDTOXml();

    }

    @Test
    @Order(1)
    void createTestXML() throws JsonProcessingException {

        mockPersonXML();
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
                    .accept(MediaType.APPLICATION_XML_VALUE)
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                    .body(personDTO)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                    .body()
                        .asString();

        PersonDTOXml createdPerson = mapper.readValue(content, PersonDTOXml.class);
        personDTO = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Dutch", createdPerson.getFirstName());
        assertEquals("van der Linde", createdPerson.getLastName());
        assertEquals("Philadelphia - Pennsylvania - EUA", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(2)
    void updateTestXML() throws JsonProcessingException {

        personDTO.setFirstName("Aiden");
        personDTO.setLastName("O'Malley");

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN,
                        TestConfigs.ORIGIN_LOCAL
                )
                .setBasePath("/api/person/v1")
                .setPort(TestConfigs.SERVER_PORT) // trocar aqui talvez
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                    .accept(MediaType.APPLICATION_XML_VALUE)
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                    .body(personDTO)
                .when()
                    .put()
                .then()
                    .statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                    .body()
                        .asString();

        PersonDTOXml createdPerson = mapper.readValue(content, PersonDTOXml.class);
        personDTO = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Aiden", createdPerson.getFirstName());
        assertEquals("O'Malley", createdPerson.getLastName());
        assertEquals("Philadelphia - Pennsylvania - EUA", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(3)
    void findByIdTestXML() throws JsonProcessingException {

        var content = given(specification)
                    .accept(MediaType.APPLICATION_XML_VALUE)
                    .pathParam("id", personDTO.getId())
                .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                    .body()
                        .asString();

        PersonDTOXml createdPerson = mapper.readValue(content, PersonDTOXml.class);
        personDTO = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Aiden", createdPerson.getFirstName());
        assertEquals("O'Malley", createdPerson.getLastName());
        assertEquals("Philadelphia - Pennsylvania - EUA", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(4)
    void disablePersonTestXML() throws JsonProcessingException {

        var content = given(specification)
                    .accept(MediaType.APPLICATION_XML_VALUE)
                    .pathParam("id", personDTO.getId())
                .when()
                    .patch("{id}")
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                    .body()
                        .asString();

        PersonDTOXml createdPerson = mapper.readValue(content, PersonDTOXml.class);
        personDTO = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Aiden", createdPerson.getFirstName());
        assertEquals("O'Malley", createdPerson.getLastName());
        assertEquals("Philadelphia - Pennsylvania - EUA", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertFalse(createdPerson.getEnabled());
    }

    @Test
    @Order(5)
    void deleteTestXML() throws JsonProcessingException {

        var content = given(specification)
                    .pathParam("id", personDTO.getId())
                .when()
                    .delete("{id}")
                .then()
                    .statusCode(204);
    }

    @Test
    @Order(6)
    void findAllTestXML() throws JsonProcessingException {

        var content = given(specification)
                    .accept(MediaType.APPLICATION_XML_VALUE)
                .when()
                    .get()
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                    .body()
                        .asString();

        List<PersonDTOXml> people = mapper.readValue(content, new TypeReference<List<PersonDTOXml>>() {});

        PersonDTOXml personOne = people.get(0);
        personDTO = personOne;

        assertNotNull(personOne.getId());
        assertTrue(personOne.getId() > 0);

        assertEquals("Lucas", personOne.getFirstName());
        assertEquals("Ferreira", personOne.getLastName());
        assertEquals("São Paulo - Brasil", personOne.getAddress());
        assertEquals("Male", personOne.getGender());
        assertTrue(personOne.getEnabled());

        PersonDTOXml personFour = people.get(4);
        personDTO = personFour;

        assertNotNull(personOne.getId());
        assertTrue(personOne.getId() > 0);

        assertEquals("Gabriel", personFour.getFirstName());
        assertEquals("Martins", personFour.getLastName());
        assertEquals("Paraná - Brasil", personFour.getAddress());
        assertEquals("Male", personFour.getGender());
        assertTrue(personFour.getEnabled());

    }

    private void mockPersonXML() {

        personDTO.setFirstName("Dutch");
        personDTO.setLastName("van der Linde");
        personDTO.setAddress("Philadelphia - Pennsylvania - EUA");
        personDTO.setGender("Male");
        personDTO.setEnabled(true);
    }

}