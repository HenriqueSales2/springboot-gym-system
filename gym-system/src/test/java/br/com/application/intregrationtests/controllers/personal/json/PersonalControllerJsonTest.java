package br.com.application.intregrationtests.controllers.personal.json;

import br.com.application.config.TestConfigs;
import br.com.application.intregrationtests.dto.personal.PersonalDTO;
import br.com.application.intregrationtests.dto.personal.wrappers.json.WrapperPersonalDTO;
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
class PersonalControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static PersonalDTO personalDTO;

    @BeforeAll
    static void setUp() {

        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        personalDTO = new PersonalDTO();

    }

    @Test
    @Order(1)
    void createPersonalTest() throws JsonProcessingException {

        mockPersonal();

        specification = new RequestSpecBuilder()
                .addHeaders(Map.of(TestConfigs.HEADER_PARAM_ORIGIN,
                        TestConfigs.ORIGIN_EXAMPLE))
                .setBasePath("/api/personal/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilters(List.of(new RequestLoggingFilter(LogDetail.ALL), new ResponseLoggingFilter(LogDetail.ALL)))
                .build();

        var content = given(specification)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(personalDTO)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                    .body()
                        .asString();

        PersonalDTO createdPersonal = objectMapper.readValue(content, PersonalDTO.class);
        personalDTO = createdPersonal;
        assertNotNull(createdPersonal.getId());
        assertTrue(createdPersonal.getId() > 0);

        assertEquals("Sadie", createdPersonal.getFirstName());
        assertEquals("Adler", createdPersonal.getLastName());
        assertEquals("Minnesota - USA", createdPersonal.getAddress());
        assertEquals("Female", createdPersonal.getGender());
        assertTrue(createdPersonal.isPersonal());

    }

    @Test
    @Order(2)
    void updatePersonalTest() throws JsonProcessingException {

        personalDTO.setLastName("O'Driscoll");

        var content = given(specification)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(personalDTO)
                .when()
                    .put()
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                    .body()
                        .asString();

        PersonalDTO createdPersonal = objectMapper.readValue(content, PersonalDTO.class);
        personalDTO = createdPersonal;

        assertNotNull(createdPersonal.getId());
        assertTrue(createdPersonal.getId() > 0);

        assertEquals("Sadie", createdPersonal.getFirstName());
        assertEquals("O'Driscoll", createdPersonal.getLastName());
        assertEquals("Minnesota - USA", createdPersonal.getAddress());
        assertEquals("Female", createdPersonal.getGender());
        assertTrue(createdPersonal.isPersonal());

    }

    @Test
    @Order(3)
    void findByIdPersonalTest() throws JsonProcessingException {

        var content = given(specification)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .pathParam("id", personalDTO.getId())
                .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                    .body()
                        .asString();

        PersonalDTO createdPersonal = objectMapper.readValue(content, PersonalDTO.class);
        personalDTO = createdPersonal;

        assertNotNull(createdPersonal.getId());
        assertTrue(createdPersonal.getId() > 0);

        assertEquals("Sadie", createdPersonal.getFirstName());
        assertEquals("O'Driscoll", createdPersonal.getLastName());
        assertEquals("Minnesota - USA", createdPersonal.getAddress());
        assertEquals("Female", createdPersonal.getGender());
        assertTrue(createdPersonal.isPersonal());

    }

    @Test
    @Order(4)
    void deletePersonalTest() throws JsonProcessingException {

        given(specification)
                    .pathParam("id", personalDTO.getId())
                .when()
                    .delete("{id}")
                .then()
                    .statusCode(204);

    }

    @Test
    @Order(5)
    void findAllTeachersTest() throws JsonProcessingException {

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

        //WrapperPersonDTO wrapper = mapper.readValue(content, WrapperPersonDTO.class);
        WrapperPersonalDTO wrapper = objectMapper.readValue(content, WrapperPersonalDTO.class);
        List<PersonalDTO> personals = wrapper.getEmbedded().getPersonals();

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

        personalDTO.setFirstName("Sadie");
        personalDTO.setLastName("Adler");
        personalDTO.setAddress("Minnesota - USA");
        personalDTO.setGender("Female");
        personalDTO.setPersonal(true);

    }
}
