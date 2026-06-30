package br.com.application.intregrationtests.controllers.auth.yaml;

import br.com.application.config.TestConfigs;
import br.com.application.intregrationtests.controllers.person.yaml.mapper.YAMLMapper;
import br.com.application.intregrationtests.dto.security.AccountCredentialsDTO;
import br.com.application.intregrationtests.dto.security.TokenDTO;
import br.com.application.intregrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerYamlTest extends AbstractIntegrationTest {

    private static YAMLMapper mapper;
    private static TokenDTO tokenDTO;

    @BeforeAll
    static void setUp() {
        mapper = new YAMLMapper();
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
    @Order(2)
    void refreshToken() {
        tokenDTO = given()
                    .config(RestAssured.config().encoderConfig(encoderConfig().encodeContentTypeAs("application/yaml", ContentType.TEXT)))
                    .basePath("/auth/refresh")
                    .port(TestConfigs.SERVER_PORT)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                    .accept(MediaType.APPLICATION_YAML_VALUE)
                    .pathParam(TestConfigs.PATH_PARAM_USERNAME, tokenDTO.getUsername())
                    .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, TestConfigs.BEARER_PREFIX + tokenDTO.getRefreshToken())
                .when()
                    .put("{username}")
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                    .body()
                        .as(TokenDTO.class, mapper);

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }
}