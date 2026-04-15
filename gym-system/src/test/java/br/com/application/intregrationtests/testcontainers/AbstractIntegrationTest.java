package br.com.application.intregrationtests.testcontainers;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;
import java.util.stream.Stream;

@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {
    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:9.1.0");

        private static void startContainers() {
            Startables.deepStart(Stream.of(mysql)).join(); // starta com os parâmetros que eu determino acima, porém, como eu não determinei nenhum ele pega os default
            // ou seja, vai inicializar o container a partir dessa instância do mysql (só para deixar bem claro)
        }

        private static Map<String, String> createConnectionConfiguration() { // aqui vai ser definido as propriedades de conexão com o banco de dados
            return Map.of( // vamos setar as propriedades, no application.yml está fixo, porém aqui vai ser gerado dinamicamente (a cada execução da aplicação, os valores mudam)
                    "spring.datasource.url", mysql.getJdbcUrl(), // pegando a URL
                    "spring.datasource.username", mysql.getUsername(), // pegando o username
                    "spring.datasource.password", mysql.getPassword() // pegando a senha
            );
        }

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            startContainers();
            ConfigurableEnvironment environment = applicationContext.getEnvironment(); // obtendo as variáveis de ambiente do Application Context
            MapPropertySource testContainers = new MapPropertySource("testcontainers",
                    (Map) createConnectionConfiguration()); // criando as configurações de conexão
            environment.getPropertySources().addFirst(testContainers); // antes de todas as configurações essas acima vão ser adicionadas primeiro
        }




    }
}
