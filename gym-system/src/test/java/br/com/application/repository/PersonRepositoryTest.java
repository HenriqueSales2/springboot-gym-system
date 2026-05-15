package br.com.application.repository;

import br.com.application.intregrationtests.testcontainers.AbstractIntegrationTest;
import br.com.application.model.Person;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

/* @ExtendWith(SpringExtension.class)
integra o Spring Framework com o JUnit 5 ,
essa anottation instrui o JUnit 5 para carregar o contexto do Spring,
permitindo o uso de componentes, beans e recursos configuráveis.
*/

/* @DataJpaTest
configura o teste para trabalhar com JPA,
e carrega apenas os componentes relacionados a camada de persistencia,
como repositórios, entidades e o contexto do banco de dados,
por padrão ele usa um banco de dados embutido,
como por exemplo um H2, para executar os testes (utilizaremos o Test Containers do Docker).
 */

/* @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
essa anottation vai garantir que o banco de dados real vai ser configurado na aplicação,
e que vai ser utilizado durante os testes.
 */

/* @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
define a ordem de execução dos testes.
 */

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    PersonRepository repository;

    private static Person person;

    @BeforeAll
    static void setUp() {
        person = new Person();
    }

    @Test
    @Order(1) // primeiro teste a ser executado
    void findPeopleByName() {
        Pageable pageable = PageRequest.of(
                0,
                12,
                Sort.by(Sort.Direction.ASC, "firstName")
        );

        person = repository.findPeopleByName("and", pageable).getContent().get(0);

        assertNotNull(person);
        assertNotNull(person.getId());
        assertNotNull(person.getId());

        assertEquals("Alejandrina", person.getFirstName());
        assertEquals("Turbayne", person.getLastName());
        assertEquals("Room 1511", person.getAddress());
        assertEquals("Female", person.getGender());
        assertTrue(person.getEnabled());
    }

    @Test
    @Order(2) // segundo teste a ser executado
    void disablePerson() {

        Long id = person.getId();
        repository.disablePerson(id);

        var result = repository.findById(id);
        person = result.get();

        assertNotNull(person);
        assertNotNull(person.getId());

        assertEquals("Alejandrina", person.getFirstName());
        assertEquals("Turbayne", person.getLastName());
        assertEquals("Room 1511", person.getAddress());
        assertEquals("Female", person.getGender());
        assertFalse(person.getEnabled());

    }
}