package br.com.application.intregrationtests.controllers.workout.yaml;

import br.com.application.config.TestConfigs;
import br.com.application.intregrationtests.controllers.person.yaml.mapper.YAMLMapper;
import br.com.application.intregrationtests.dto.security.AccountCredentialsDTO;
import br.com.application.intregrationtests.dto.security.TokenDTO;
import br.com.application.intregrationtests.dto.workout.WorkoutDTO;
import br.com.application.intregrationtests.dto.workout.wrappers.xml.PagedModelWorkout;
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
public class WorkoutControllerYamlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static YAMLMapper mapper;
    private static WorkoutDTO workoutDTO;
    private static TokenDTO tokenDTO;

    @BeforeAll
    static void setUp() {
        mapper = new YAMLMapper();
        workoutDTO = new WorkoutDTO();
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
    void createWorkoutTestYaml() {
        mockWorkoutYaml();

        specification = new RequestSpecBuilder()
                .addHeaders(Map.of(TestConfigs.HEADER_PARAM_ORIGIN,
                        TestConfigs.ORIGIN_EXAMPLE))
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, TestConfigs.BEARER_PREFIX + tokenDTO.getAccessToken())
                .setBasePath("/api/workout/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilters(List.of(new RequestLoggingFilter(LogDetail.ALL), new ResponseLoggingFilter(LogDetail.ALL)))
                .build();

        var content = given(specification)
                    .config(RestAssured.config().encoderConfig(encoderConfig().encodeContentTypeAs("application/yaml", ContentType.TEXT)))
                    .accept(MediaType.APPLICATION_YAML_VALUE)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                    .body(workoutDTO, mapper)
                .when()
                    .post()
                .then()
                    .statusCode(201)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                    .body()
                        .as(WorkoutDTO.class, mapper);

        workoutDTO = content;

        assertNotNull(content.getId());
        assertTrue(content.getId() > 0);

        assertEquals("Barbell Curl", content.getExerciseName());
        assertEquals("Biceps", content.getMuscleGroup());
        assertEquals("Barbell", content.getEquipment());
        assertEquals("Beginner", content.getDifficulty());
    }

    @Test
    @Order(2)
    void updateWorkoutTestYaml() {
        workoutDTO.setExerciseName("Hammer Curl");
        workoutDTO.setEquipment("Dumbbells");

        var content = given(specification)
                    .config(RestAssured.config().encoderConfig(encoderConfig().encodeContentTypeAs("application/yaml", ContentType.TEXT)))
                    .accept(MediaType.APPLICATION_YAML_VALUE)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                    .body(workoutDTO, mapper)
                .when()
                    .put()
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                    .body()
                        .as(WorkoutDTO.class, mapper);
        
        workoutDTO = content;

        assertNotNull(content.getId());
        assertTrue(content.getId() > 0);

        assertEquals("Hammer Curl", content.getExerciseName());
        assertEquals("Biceps", content.getMuscleGroup());
        assertEquals("Dumbbells", content.getEquipment());
        assertEquals("Beginner", content.getDifficulty());
    }

    @Test
    @Order(3)
    void findByIdWorkoutTestYaml() {
        var content = given(specification)
                    .config(RestAssured.config().encoderConfig(encoderConfig().encodeContentTypeAs("application/yaml", ContentType.TEXT)))
                    .accept(MediaType.APPLICATION_YAML_VALUE)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                .pathParam("id", workoutDTO.getId())
                .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                    .body()
                        .as(WorkoutDTO.class, mapper);

        workoutDTO = content;

        assertNotNull(content.getId());
        assertTrue(content.getId() > 0);

        assertEquals("Hammer Curl", content.getExerciseName());
        assertEquals("Biceps", content.getMuscleGroup());
        assertEquals("Dumbbells", content.getEquipment());
        assertEquals("Beginner", content.getDifficulty());
    }

    @Test
    @Order(4)
    void deleteWorkoutTestYaml() {
        given(specification)
                    .pathParam("id", workoutDTO.getId())
                .when()
                    .delete("{id}")
                .then()
                    .statusCode(204);

    }

    @Test
    @Order(5)
    void findAllWorkoutsTestYaml() {
        var response = given(specification)
                    .accept(MediaType.APPLICATION_YAML_VALUE)
                    .queryParams("page", 0, "size", 6, "direction", "asc")
                .when()
                    .get()
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                    .body()
                        .as(PagedModelWorkout.class, mapper);

        List<WorkoutDTO> workouts = response.getContent();

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

    private void mockWorkoutYaml() {
        workoutDTO.setExerciseName("Barbell Curl");
        workoutDTO.setMuscleGroup("Biceps");
        workoutDTO.setEquipment("Barbell");
        workoutDTO.setDifficulty("Beginner");
    }
}