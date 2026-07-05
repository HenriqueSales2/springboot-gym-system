package br.com.application.intregrationtests.controllers.workout.json;

import br.com.application.config.TestConfigs;
import br.com.application.intregrationtests.dto.security.AccountCredentialsDTO;
import br.com.application.intregrationtests.dto.security.TokenDTO;
import br.com.application.intregrationtests.dto.workout.WorkoutDTO;
import br.com.application.intregrationtests.dto.workout.wrappers.json.WrapperWorkoutDTO;
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
class WorkoutControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static WorkoutDTO workoutDTO;
    private static TokenDTO tokenDTO;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        workoutDTO = new WorkoutDTO();
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
    void createWorkoutTest() throws JsonProcessingException {
        mockWorkout();

        specification = new RequestSpecBuilder()
                .addHeaders(Map.of(TestConfigs.HEADER_PARAM_ORIGIN,
                        TestConfigs.ORIGIN_EXAMPLE))
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, TestConfigs.BEARER_PREFIX + tokenDTO.getAccessToken())
                .setBasePath(TestConfigs.BASEPATH_PARAM_WORKOUT)
                .setPort(TestConfigs.SERVER_PORT)
                .addFilters(List.of(new RequestLoggingFilter(LogDetail.ALL), new ResponseLoggingFilter(LogDetail.ALL)))
                .build();

        var content = given(specification)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(workoutDTO)
                .when()
                    .post()
                .then()
                    .statusCode(201)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                    .body()
                        .asString();

        WorkoutDTO createdWorkout = objectMapper.readValue(content, WorkoutDTO.class);
        workoutDTO = createdWorkout;
        assertNotNull(createdWorkout.getId());
        assertTrue(createdWorkout.getId() > 0);

        assertEquals("Bench Press", createdWorkout.getExerciseName());
        assertEquals("Chest", createdWorkout.getMuscleGroup());
        assertEquals("Barbell", createdWorkout.getEquipment());
        assertEquals("Intermediate", createdWorkout.getDifficulty());
    }

    @Test
    @Order(2)
    void updateWorkoutTest() throws JsonProcessingException {
        workoutDTO.setExerciseName("Incline Bench Press");

        var content = given(specification)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(workoutDTO)
                .when()
                    .put()
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                    .body()
                        .asString();

        WorkoutDTO createdWorkout = objectMapper.readValue(content, WorkoutDTO.class);
        workoutDTO = createdWorkout;

        assertNotNull(createdWorkout.getId());
        assertTrue(createdWorkout.getId() > 0);

        assertEquals("Incline Bench Press", createdWorkout.getExerciseName());
        assertEquals("Chest", createdWorkout.getMuscleGroup());
        assertEquals("Barbell", createdWorkout.getEquipment());
        assertEquals("Intermediate", createdWorkout.getDifficulty());
    }

    @Test
    @Order(3)
    void findByIdWorkoutTest() throws JsonProcessingException {
        var content = given(specification)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .pathParam("id", workoutDTO.getId())
                .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                    .body()
                        .asString();

        WorkoutDTO createdWorkout = objectMapper.readValue(content, WorkoutDTO.class);
        workoutDTO = createdWorkout;

        assertNotNull(createdWorkout.getId());
        assertTrue(createdWorkout.getId() > 0);

        assertEquals("Incline Bench Press", createdWorkout.getExerciseName());
        assertEquals("Chest", createdWorkout.getMuscleGroup());
        assertEquals("Barbell", createdWorkout.getEquipment());
        assertEquals("Intermediate", createdWorkout.getDifficulty());
    }

    @Test
    @Order(4)
    void deleteWorkoutTest() {
        given(specification)
                    .pathParam("id", workoutDTO.getId())
                .when()
                    .delete("{id}")
                .then()
                    .statusCode(204);
    }

    @Test
    @Order(5)
    void findAllWorkoutsTest() throws JsonProcessingException {
        var content = given(specification)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .queryParams("page", 0, "size", 6, "direction", "asc")
                .when()
                    .get()
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                    .body()
                        .asString();

        WrapperWorkoutDTO wrapper = objectMapper.readValue(content, WrapperWorkoutDTO.class);
        List<WorkoutDTO> workouts = wrapper.getEmbedded().getWorkouts();

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

    private void mockWorkout() {
        workoutDTO.setExerciseName("Bench Press");
        workoutDTO.setMuscleGroup("Chest");
        workoutDTO.setEquipment("Barbell");
        workoutDTO.setDifficulty("Intermediate");
    }
}
