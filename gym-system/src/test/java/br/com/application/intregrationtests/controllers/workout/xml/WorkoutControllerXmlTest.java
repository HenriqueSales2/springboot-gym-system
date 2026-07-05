package br.com.application.intregrationtests.controllers.workout.xml;

import br.com.application.config.TestConfigs;
import br.com.application.intregrationtests.dto.security.AccountCredentialsDTO;
import br.com.application.intregrationtests.dto.security.TokenDTO;
import br.com.application.intregrationtests.dto.workout.WorkoutDTO;
import br.com.application.intregrationtests.dto.workout.wrappers.xml.PagedModelWorkout;
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

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WorkoutControllerXmlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static XmlMapper mapper;
    private static WorkoutDTO workoutDTO;
    private static TokenDTO tokenDTO;

    @BeforeAll
    static void setUp() {
        mapper = new XmlMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        workoutDTO = new WorkoutDTO();
        tokenDTO = new TokenDTO();
    }

    @Test
    @Order(0)
    void sigIn() throws IOException {
        AccountCredentialsDTO credentials = new AccountCredentialsDTO("john", "admin123");
        String credentialsXml = mapper.writeValueAsString(credentials);

        var content = given()
                    .basePath("auth/signin")
                    .port(TestConfigs.SERVER_PORT)
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                    .accept(MediaType.APPLICATION_XML_VALUE)
                    .body(credentialsXml)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                    .body()
                        .asString();

        tokenDTO = mapper.readValue( content, TokenDTO.class);

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }

    @Test
    @Order(1)
    void createWorkoutTestXml() throws JsonProcessingException {
        mockWorkoutXml();

        specification = new RequestSpecBuilder()
                .addHeaders(Map.of(TestConfigs.HEADER_PARAM_ORIGIN,
                        TestConfigs.ORIGIN_EXAMPLE))
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, TestConfigs.BEARER_PREFIX + tokenDTO.getAccessToken())
                .setBasePath(TestConfigs.BASEPATH_PARAM_WORKOUT)
                .setPort(TestConfigs.SERVER_PORT)
                .addFilters(List.of(new RequestLoggingFilter(LogDetail.ALL), new ResponseLoggingFilter(LogDetail.ALL)))
                .build();

        var content = given(specification)
                    .accept(MediaType.APPLICATION_XML_VALUE)
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                    .body(workoutDTO)
                .when()
                    .post()
                .then()
                    .statusCode(201)
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                    .body()
                        .asString();

        WorkoutDTO createdWorkout = mapper.readValue(content, WorkoutDTO.class);
        workoutDTO = createdWorkout;

        assertNotNull(createdWorkout.getId());
        assertTrue(createdWorkout.getId() > 0);

        assertEquals("Deadlift", createdWorkout.getExerciseName());
        assertEquals("Back", createdWorkout.getMuscleGroup());
        assertEquals("Barbell", createdWorkout.getEquipment());
        assertEquals("Advanced", createdWorkout.getDifficulty());
    }

    @Test
    @Order(2)
    void updateWorkoutTestXml() throws JsonProcessingException {
        workoutDTO.setExerciseName("Pull Up");
        workoutDTO.setEquipment("Bodyweight");

        var content = given(specification)
                    .accept(MediaType.APPLICATION_XML_VALUE)
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                    .body(workoutDTO)
                .when()
                    .put()
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                    .body()
                        .asString();

        WorkoutDTO createdWorkout = mapper.readValue(content, WorkoutDTO.class);
        workoutDTO = createdWorkout;

        assertNotNull(createdWorkout.getId());
        assertTrue(createdWorkout.getId() > 0);


        assertEquals("Pull Up", createdWorkout.getExerciseName());
        assertEquals("Back", createdWorkout.getMuscleGroup());
        assertEquals("Bodyweight", createdWorkout.getEquipment());
        assertEquals("Advanced", createdWorkout.getDifficulty());
    }

    @Test
    @Order(3)
    void findByIdWorkoutTestXml() throws JsonProcessingException {
        var content = given(specification)
                    .accept(MediaType.APPLICATION_XML_VALUE)
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                    .pathParam("id", workoutDTO.getId())
                .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                    .body()
                        .asString();

        WorkoutDTO createdWorkout = mapper.readValue(content, WorkoutDTO.class);
        workoutDTO = createdWorkout;

        assertNotNull(createdWorkout.getId());
        assertTrue(createdWorkout.getId() > 0);

        assertEquals("Pull Up", createdWorkout.getExerciseName());
        assertEquals("Back", createdWorkout.getMuscleGroup());
        assertEquals("Bodyweight", createdWorkout.getEquipment());
        assertEquals("Advanced", createdWorkout.getDifficulty());
    }

    @Test
    @Order(4)
    void deleteWorkoutTestXml() throws JsonProcessingException {
        given(specification)
                    .pathParam("id", workoutDTO.getId())
                .when()
                    .delete("{id}")
                .then()
                    .statusCode(204);
    }

    @Test
    @Order(5)
    void findAllWorkoutsTestXml() throws JsonProcessingException {
        var content = given(specification)
                    .accept(MediaType.APPLICATION_XML_VALUE)
                    .queryParams("page", 0, "size", 6, "direction", "asc")
                .when()
                    .get()
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                    .body()
                        .asString();

        PagedModelWorkout wrapper = mapper.readValue(content, PagedModelWorkout.class);
        List<WorkoutDTO> workouts = wrapper.getContent();

        WorkoutDTO workoutOne = workouts.get(0);
        workoutDTO = workoutOne;

        assertNotNull(workoutOne.getId());
        assertTrue(workoutOne.getId() > 0);

        assertEquals("Alternating Dumbbell Curl", workoutOne.getExerciseName());
        assertEquals("Biceps", workoutOne.getMuscleGroup());
        assertEquals("Dumbbells", workoutOne.getEquipment());
        assertEquals("Beginner", workoutOne.getDifficulty());

        WorkoutDTO workoutFour = workouts.get(4);
        workoutDTO = workoutFour;

        assertNotNull(workoutFour.getId());
        assertTrue(workoutFour.getId() > 0);

        assertEquals("Barbell Squat", workoutFour.getExerciseName());
        assertEquals("Legs", workoutFour.getMuscleGroup());
        assertEquals("Barbell", workoutFour.getEquipment());
        assertEquals("Advanced", workoutFour.getDifficulty());
    }

    private void mockWorkoutXml() {
        workoutDTO.setExerciseName("Deadlift");
        workoutDTO.setMuscleGroup("Back");
        workoutDTO.setEquipment("Barbell");
        workoutDTO.setDifficulty("Advanced");
    }
}