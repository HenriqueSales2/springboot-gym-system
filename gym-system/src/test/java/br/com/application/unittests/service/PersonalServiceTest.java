package br.com.application.unittests.service;

import br.com.application.data.dto.PersonDTO;
import br.com.application.data.dto.PersonalDTO;
import br.com.application.exception.RequiredObjectIsNullException;
import br.com.application.model.Person;
import br.com.application.model.Personal;
import br.com.application.repository.PersonalRepository;
import br.com.application.service.PersonalService;
import br.com.application.unittests.mapper.mocks.MockPersonal;
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
class PersonalServiceTest {

    MockPersonal input; // vamos passar como parâmetro para os testes

    @InjectMocks
    private PersonalService service;

    @Mock
    PersonalRepository repository;

    @BeforeEach
    void setUp() {
        input = new MockPersonal();
        MockitoAnnotations.openMocks(this); // essa linha é muito importante, pois sem isso, é inviável injetar o Repository e Service na classe de testes
    }

    @Test
    void findPersonalById() { // testa se todos os campos do Objeto Person e os Links estão funcionando, caso não estejam, retorna um erro
        Personal personal =  input.mockEntity(1);
        personal.setId(1L);
        when(repository // dizendo ao Mockito o que ele deve fazer quando o Repositório for invocado
                .findById(1L)) // nesse caso é chamar o método findById (passando como parâmetro o id, que seria um mock)
                .thenReturn(Optional.of(personal)); // e o que ele deve retornar (ele vai retornar uma instancia de "person")

        var result = service.findPersonalById(1L);

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

        Personal personal =  input.mockEntity(1);
        Personal persisted = personal;
        persisted.setId(1L);

        PersonalDTO dto = input.mockDTO(1);

        personal.setId(1L);
        when(repository // dizendo ao Mockito o que ele deve fazer quando o Repositório for invocado
                .save(personal)) // nesse caso é chamar o método findById (passando como parâmetro o id, que seria um mock)
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

        Personal personal =  input.mockEntity(1);
        Personal persisted = personal;
        persisted.setId(1L);

        PersonalDTO dto = input.mockDTO(1);

        personal.setId(1L);
        when(repository // dizendo ao Mockito o que ele deve fazer quando o Repositório for invocado
                .findById(1L)) // nesse caso é chamar o método findById (passando como parâmetro o id, que seria um mock)
                .thenReturn(Optional.of(personal));
        when(repository.save(personal)).thenReturn(persisted);
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
        Personal personal = input.mockEntity(1);
        personal.setId(1L);
        when(repository // dizendo ao Mockito o que ele deve fazer quando o Repositório for invocado
                .findById(1L)) // nesse caso é chamar o método findById (passando como parâmetro o id, que seria um mock)
                .thenReturn(Optional.of(personal)); // e o que ele deve retornar (ele vai retornar uma instancia de "person")

        service.delete(1L);
        verify(repository, times(1)).findById(anyLong()); // vai invocar o número de vezes que eu chamar o método FindById
        verify(repository, times(1)).delete(any(Personal.class)); // vai invocar o número de vezes que eu chamar o método delete passando como parâmetro a entidade Person
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findAll() {

        List<Personal> list = input.mockEntityList();
        when(repository.findAll()).thenReturn(list);
        List<PersonalDTO> teachers =  service.findAllTeachers();

        assertEquals(14, teachers.size());

        var personalOne = teachers.get(1);

        assertNotNull(personalOne); // verificando se os objetos são coerentes
        assertNotNull(personalOne.getId()); // verificando se os id é coerente
        assertNotNull(personalOne.getLinks()); // verificando se o link é coerente
        assertNotNull(personalOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self") // verificando se a propriedade "self" é coerente em ambas as partes (self é o findById)
                        && link.getHref().endsWith("/api/person/v1/1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("GET") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(personalOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll") // verificando se a propriedade "findAll" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/person/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("GET") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(personalOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create") // verificando se a propriedade "create" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/person/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("POST") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(personalOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update") // verificando se a propriedade "update" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/person/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("PUT") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(personalOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete") // verificando se a propriedade "delete" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/person/v1/1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("DELETE") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );

        assertEquals("Address Test1", personalOne.getAddress());
        assertEquals("First Name Test1", personalOne.getFirstName());
        assertEquals("Female", personalOne.getGender());
        assertEquals("Last Name Test1", personalOne.getLastName());


        var personalFour = teachers.get(4);

        assertNotNull(personalFour); // verificando se os objetos são coerentes
        assertNotNull(personalFour.getId()); // verificando se os id é coerente
        assertNotNull(personalFour.getLinks()); // verificando se o link é coerente
        assertNotNull(personalFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self") // verificando se a propriedade "self" é coerente em ambas as partes (self é o findById)
                        && link.getHref().endsWith("/api/person/v1/4") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("GET") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(personalFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll") // verificando se a propriedade "findAll" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/person/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("GET") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(personalFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create") // verificando se a propriedade "create" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/person/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("POST") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(personalFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update") // verificando se a propriedade "update" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/person/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("PUT") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(personalFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete") // verificando se a propriedade "delete" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/person/v1/4") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("DELETE") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );

        assertEquals("Address Test4", personalFour.getAddress());
        assertEquals("First Name Test4", personalFour.getFirstName());
        assertEquals("Male", personalFour.getGender());
        assertEquals("Last Name Test4", personalFour.getLastName());


        var personalSeven = teachers.get(7);

        assertNotNull(personalSeven); // verificando se os objetos são coerentes
        assertNotNull(personalSeven.getId()); // verificando se os id é coerente
        assertNotNull(personalSeven.getLinks()); // verificando se o link é coerente
        assertNotNull(personalSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self") // verificando se a propriedade "self" é coerente em ambas as partes (self é o findById)
                        && link.getHref().endsWith("/api/person/v1/7") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("GET") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(personalSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll") // verificando se a propriedade "findAll" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/person/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("GET") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(personalSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create") // verificando se a propriedade "create" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/person/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("POST") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(personalSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update") // verificando se a propriedade "update" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/person/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("PUT") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(personalSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete") // verificando se a propriedade "delete" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/person/v1/7") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("DELETE") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );

        assertEquals("Address Test7", personalSeven.getAddress());
        assertEquals("First Name Test7", personalSeven.getFirstName());
        assertEquals("Female", personalSeven.getGender());
        assertEquals("Last Name Test7", personalSeven.getLastName());

    }
}