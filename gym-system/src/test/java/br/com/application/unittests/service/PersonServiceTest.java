package br.com.application.unittests.service;

import br.com.application.data.dto.PersonDTO;
import br.com.application.exception.RequiredObjectIsNullException;
import br.com.application.model.Person;
import br.com.application.repository.PersonRepository;
import br.com.application.service.PersonService;
import br.com.application.unittests.mapper.mocks.MockPerson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) // o ciclo de vida dos objetos vão durar apenas para essa classe
@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    MockPerson input; // vamos passar como parâmetro para os testes

    @InjectMocks
    private PersonService service;

    @Mock
    PersonRepository repository;

    @BeforeEach
    void setUp() {
        input = new MockPerson();
        MockitoAnnotations.openMocks(this); // essa linha é muito importante, pois sem isso, é inviável injetar o Repository e Service na classe de testes
    }

    @Test
    void findById() { // testa se todos os campos do Objeto Person e os Links estão funcionando, caso não estejam, retorna um erro
        Person person =  input.mockEntity(1);
        person.setId(1L);
        when(repository // dizendo ao Mockito o que ele deve fazer quando o Repositório for invocado
                .findById(1L)) // nesse caso é chamar o método findById (passando como parâmetro o id, que seria um mock)
                .thenReturn(Optional.of(person)); // e o que ele deve retornar (ele vai retornar uma instancia de "person")

        var result = service.findById(1L);

        assertNotNull(result); // verificando se os objetos são coerentes
        assertNotNull(result.getId()); // verificando se os id é coerente
        assertNotNull(result.getLinks()); // verificando se o link é coerente
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self") // verificando se a propriedade "self" é coerente em ambas as partes (self é o findById)
                                && link.getHref().endsWith("/api/person/v1/1") // verificando se a propriedade "href" é coerente em ambas as partes
                                && link.getType().equals("GET") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll") // verificando se a propriedade "findAll" é coerente em ambas as partes
                                && link.getHref().endsWith("/api/person/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                                && link.getType().equals("GET") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create") // verificando se a propriedade "create" é coerente em ambas as partes
                                && link.getHref().endsWith("/api/person/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                                && link.getType().equals("POST") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update") // verificando se a propriedade "update" é coerente em ambas as partes
                                && link.getHref().endsWith("/api/person/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                                && link.getType().equals("PUT") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete") // verificando se a propriedade "delete" é coerente em ambas as partes
                                && link.getHref().endsWith("/api/person/v1/1") // verificando se a propriedade "href" é coerente em ambas as partes
                                && link.getType().equals("DELETE") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );

        assertEquals("Address Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Female", result.getGender());
        assertEquals("Last Name Test1", result.getLastName());


    }

    @Test
    void create() {

        Person person =  input.mockEntity(1);
        Person persisted = person;
        persisted.setId(1L);

        PersonDTO dto = input.mockDTO(1);

        person.setId(1L);
        when(repository // dizendo ao Mockito o que ele deve fazer quando o Repositório for invocado
                .save(person)) // nesse caso é chamar o método findById (passando como parâmetro o id, que seria um mock)
                .thenReturn(persisted); // e o que ele deve retornar (ele vai retornar uma instancia de "person", dessa vez não é Optional)

        var result = service.create(dto);

        assertNotNull(result); // verificando se os objetos são coerentes
        assertNotNull(result.getId()); // verificando se os id é coerente
        assertNotNull(result.getLinks()); // verificando se o link é coerente
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self") // verificando se a propriedade "self" é coerente em ambas as partes (self é o findById)
                        && link.getHref().endsWith("/api/person/v1/1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("GET") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll") // verificando se a propriedade "findAll" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/person/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("GET") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create") // verificando se a propriedade "create" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/person/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("POST") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update") // verificando se a propriedade "update" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/person/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("PUT") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete") // verificando se a propriedade "delete" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/person/v1/1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("DELETE") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );

        assertEquals("Address Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Female", result.getGender());
        assertEquals("Last Name Test1", result.getLastName());


    }

    @Test
    void testCreateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class,
                () -> {
            service.create(null);
                });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void update() {

        Person person =  input.mockEntity(1);
        Person persisted = person;
        persisted.setId(1L);

        PersonDTO dto = input.mockDTO(1);

        person.setId(1L);
        when(repository // dizendo ao Mockito o que ele deve fazer quando o Repositório for invocado
                .findById(1L)) // nesse caso é chamar o método findById (passando como parâmetro o id, que seria um mock)
                .thenReturn(Optional.of(person));
        when(repository.save(person)).thenReturn(persisted);
        var result = service.update(dto);

        assertNotNull(result); // verificando se os objetos são coerentes
        assertNotNull(result.getId()); // verificando se os id é coerente
        assertNotNull(result.getLinks()); // verificando se o link é coerente
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self") // verificando se a propriedade "self" é coerente em ambas as partes (self é o findById)
                        && link.getHref().endsWith("/api/person/v1/1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("GET") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll") // verificando se a propriedade "findAll" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/person/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("GET") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create") // verificando se a propriedade "create" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/person/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("POST") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update") // verificando se a propriedade "update" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/person/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("PUT") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete") // verificando se a propriedade "delete" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/person/v1/1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("DELETE") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );

        assertEquals("Address Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Female", result.getGender());
        assertEquals("Last Name Test1", result.getLastName());

    }

    @Test
    void testUpdateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class,
                () -> {
                    service.update(null);
                });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void delete() {
        Person person = input.mockEntity(1);
        person.setId(1L);
        when(repository // dizendo ao Mockito o que ele deve fazer quando o Repositório for invocado
                .findById(1L)) // nesse caso é chamar o método findById (passando como parâmetro o id, que seria um mock)
                .thenReturn(Optional.of(person)); // e o que ele deve retornar (ele vai retornar uma instancia de "person")

        service.delete(1L);
        verify(repository, times(1)).findById(anyLong()); // vai invocar o número de vezes que eu chamar o método FindById
        verify(repository, times(1)).delete(any(Person.class)); // vai invocar o número de vezes que eu chamar o método delete passando como parâmetro a entidade Person
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findAll() {

        List<Person> list = input.mockEntityList();
        when(repository.findAll()).thenReturn(list);
        List<PersonDTO> people =  service.findAll();

        assertEquals(14, people.size());

        var personOne = people.get(1);

        assertNotNull(personOne); // verificando se os objetos são coerentes
        assertNotNull(personOne.getId()); // verificando se os id é coerente
        assertNotNull(personOne.getLinks()); // verificando se o link é coerente
        assertNotNull(personOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self") // verificando se a propriedade "self" é coerente em ambas as partes (self é o findById)
                        && link.getHref().endsWith("/api/person/v1/1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("GET") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(personOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll") // verificando se a propriedade "findAll" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/person/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("GET") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(personOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create") // verificando se a propriedade "create" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/person/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("POST") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(personOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update") // verificando se a propriedade "update" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/person/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("PUT") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(personOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete") // verificando se a propriedade "delete" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/person/v1/1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("DELETE") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );

        assertEquals("Address Test1", personOne.getAddress());
        assertEquals("First Name Test1", personOne.getFirstName());
        assertEquals("Female", personOne.getGender());
        assertEquals("Last Name Test1", personOne.getLastName());


        var personFour = people.get(4);

        assertNotNull(personFour); // verificando se os objetos são coerentes
        assertNotNull(personFour.getId()); // verificando se os id é coerente
        assertNotNull(personFour.getLinks()); // verificando se o link é coerente
        assertNotNull(personFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self") // verificando se a propriedade "self" é coerente em ambas as partes (self é o findById)
                        && link.getHref().endsWith("/api/person/v1/4") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("GET") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(personFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll") // verificando se a propriedade "findAll" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/person/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("GET") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(personFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create") // verificando se a propriedade "create" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/person/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("POST") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(personFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update") // verificando se a propriedade "update" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/person/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("PUT") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(personFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete") // verificando se a propriedade "delete" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/person/v1/4") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("DELETE") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );

        assertEquals("Address Test4", personFour.getAddress());
        assertEquals("First Name Test4", personFour.getFirstName());
        assertEquals("Male", personFour.getGender());
        assertEquals("Last Name Test4", personFour.getLastName());


        var personSeven = people.get(7);

        assertNotNull(personSeven); // verificando se os objetos são coerentes
        assertNotNull(personSeven.getId()); // verificando se os id é coerente
        assertNotNull(personSeven.getLinks()); // verificando se o link é coerente
        assertNotNull(personSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self") // verificando se a propriedade "self" é coerente em ambas as partes (self é o findById)
                        && link.getHref().endsWith("/api/person/v1/7") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("GET") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(personSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll") // verificando se a propriedade "findAll" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/person/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("GET") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(personSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create") // verificando se a propriedade "create" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/person/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("POST") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(personSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update") // verificando se a propriedade "update" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/person/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("PUT") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(personSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete") // verificando se a propriedade "delete" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/person/v1/7") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("DELETE") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );

        assertEquals("Address Test7", personSeven.getAddress());
        assertEquals("First Name Test7", personSeven.getFirstName());
        assertEquals("Female", personSeven.getGender());
        assertEquals("Last Name Test7", personSeven.getLastName());

    }
}