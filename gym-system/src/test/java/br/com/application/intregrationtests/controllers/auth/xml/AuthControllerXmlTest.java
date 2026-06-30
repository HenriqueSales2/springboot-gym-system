package br.com.application.intregrationtests.controllers.auth.xml;

import br.com.application.config.TestConfigs;
import br.com.application.intregrationtests.dto.security.AccountCredentialsDTO;
import br.com.application.intregrationtests.dto.security.TokenDTO;
import br.com.application.intregrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerXmlTest extends AbstractIntegrationTest {

    private static XmlMapper mapper;
    private static TokenDTO tokenDTO;

    @BeforeAll
    static void setUp() {
        mapper = new XmlMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        tokenDTO = new TokenDTO();
    }

    @Test
    @Order(1)
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
    @Order(2)
    void refreshToken() throws JsonProcessingException {

        var content = given()
                    .basePath("/auth/refresh")
                    .port(TestConfigs.SERVER_PORT)
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                    .accept(MediaType.APPLICATION_XML_VALUE)
                    .pathParam(TestConfigs.PATH_PARAM_USERNAME, tokenDTO.getUsername())
                    .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, TestConfigs.BEARER_PREFIX + tokenDTO.getRefreshToken())
                .when()
                    .put("{username}")
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                    .body()
                        .asString();

        tokenDTO = mapper.readValue(content, TokenDTO.class);

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }
}