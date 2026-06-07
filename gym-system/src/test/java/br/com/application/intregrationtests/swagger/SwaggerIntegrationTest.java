package br.com.application.intregrationtests.swagger;

import br.com.application.config.TestConfigs;
import br.com.application.intregrationtests.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) // vamos definir a porta que o spring boot vai utilizar para os testes
class SwaggerIntegrationTest extends AbstractIntegrationTest {

	@Test
	void shouldDisplaySwaggerUIPage() {
		var content = given() // armazenando todo esse conteúdo em uma variável
				.basePath("/swagger-ui/index.html")
					.port(TestConfigs.SERVER_PORT)
				.when() // quando executar uma operação
					.get()// do tipo get
				.then() // então
					.statusCode(200)// eu espero a resposta de statusCode 200 OK (significa que deu tudo certo)
				.extract()
					.body() // pegar o conteúdo do body (corpo)
						.asString(); // transformar em String

		assertTrue(content.contains("Swagger UI"));

	}

}
