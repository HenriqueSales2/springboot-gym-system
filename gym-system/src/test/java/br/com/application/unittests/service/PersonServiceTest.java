package br.com.application.unittests.service;

import br.com.application.data.dto.PersonDTO;
import br.com.application.exception.RequiredObjectIsNullException;
import br.com.application.model.Person;
import br.com.application.repository.PersonRepository;
import br.com.application.service.person.PersonService;
import br.com.application.unittests.mapper.mocks.MockPerson;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonServiceTest {

    MockPerson input;

    @InjectMocks
    private PersonService service;

    @Mock
    PersonRepository repository;

    @Mock
    private PagedResourcesAssembler<PersonDTO> assembler;

    @BeforeEach
    void setUp() {
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Order(1)
    void createTest() {
        PersonDTO dto = input.mockDTO(1);

        when(repository
                .save(any(Person.class)))
                .thenReturn(input.mockEntity(1));

        var result = service.create(dto);

        assertPerson(result, 1);

        assertHasLink(result, "self", "/api/person/v1/1", "GET");
        assertHasLink(result, "findAll", "/api/person/v1", "GET");
        assertHasLink(result, "create", "/api/person/v1", "POST");
        assertHasLink(result, "update", "/api/person/v1", "PUT");
        assertHasLink(result, "delete", "/api/person/v1/1", "DELETE");
    }

    @Test
    @Order(2)
    void createWithNullPersonTest() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class,
                () -> {
            service.create(null);
                });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @Order(3)
    void updateTest() {
        Person person =  input.mockEntity(1);
        Person persisted = person;
        persisted.setId(1L);

        PersonDTO dto = input.mockDTO(1);

        person.setId(1L);
        when(repository
                .findById(1L))
                .thenReturn(Optional.of(person));
        when(repository.save(person)).thenReturn(persisted);
        var result = service.update(dto);

        assertPerson(result, 1);

        assertHasLink(result, "self", "/api/person/v1/1", "GET");
        assertHasLink(result, "findAll", "/api/person/v1", "GET");
        assertHasLink(result, "create", "/api/person/v1", "POST");
        assertHasLink(result, "update", "/api/person/v1", "PUT");
        assertHasLink(result, "delete", "/api/person/v1/1", "DELETE");
    }

    @Test
    @Order(4)
    void updateWithNullPersonTest() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class,
                () -> {
                    service.update(null);
                });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @Order(5)
    void findByIdTest() {
        Person person =  input.mockEntity(1);
        person.setId(1L);
        when(repository
                .findById(1L))
                .thenReturn(Optional.of(person));

        var result = service.findById(1L);

        assertPerson(result, 1);

        assertHasLink(result, "self", "/api/person/v1/1", "GET");
        assertHasLink(result, "findAll", "/api/person/v1", "GET");
        assertHasLink(result, "create", "/api/person/v1", "POST");
        assertHasLink(result, "update", "/api/person/v1", "PUT");
        assertHasLink(result, "delete", "/api/person/v1/1", "DELETE");
    }

    @Test
    @Order(6)
    void deleteTest() {
        Person person = input.mockEntity(1);
        person.setId(1L);
        when(repository
                .findById(1L))
                .thenReturn(Optional.of(person));

        service.delete(1L);
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(Person.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    @Order(7)
    void findAllTest() {
        var page = 0;
        var size = 12;
        var direction = "asc";

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "firstName"));

        List<Person> list = input.mockEntityList();

        Page<Person> pageImpl = new PageImpl<>(list);

        PagedModel<EntityModel<PersonDTO>> pagedModel = mock(PagedModel.class);

        when(repository.findAll(any(Pageable.class)))
                .thenReturn(pageImpl);

        when(assembler.toModel(any(Page.class), any(Link.class)))
                .thenReturn(pagedModel);

        service.findAll(pageable);

        ArgumentCaptor<Page<PersonDTO>> pageCaptor = ArgumentCaptor.forClass(Page.class);

        verify(assembler).toModel(pageCaptor.capture(), any(Link.class));

        Page<PersonDTO> pageSentToAssembler = pageCaptor.getValue();

        assertEquals(14, pageSentToAssembler.getContent().size());

        PersonDTO personOne = pageSentToAssembler.getContent().get(1);

        assertPerson(personOne, 1);

        assertHasLink(personOne, "self", "/api/person/v1/1", "GET");
        assertHasLink(personOne, "findAll", "/api/person/v1", "GET");
        assertHasLink(personOne, "create", "/api/person/v1", "POST");
        assertHasLink(personOne, "update", "/api/person/v1", "PUT");
        assertHasLink(personOne, "delete", "/api/person/v1/1", "DELETE");

        PersonDTO personFour = pageSentToAssembler.getContent().get(4);

        assertPerson(personFour, 4);

        assertHasLink(personFour, "self", "/api/person/v1/4", "GET");
        assertHasLink(personFour, "findAll", "/api/person/v1", "GET");
        assertHasLink(personFour, "create", "/api/person/v1", "POST");
        assertHasLink(personFour, "update", "/api/person/v1", "PUT");
        assertHasLink(personFour, "delete", "/api/person/v1/4", "DELETE");

        PersonDTO personSeven = pageSentToAssembler.getContent().get(7);

        assertPerson(personSeven, 7);

        assertHasLink(personSeven, "self", "/api/person/v1/7", "GET");
        assertHasLink(personSeven, "findAll", "/api/person/v1", "GET");
        assertHasLink(personSeven, "create", "/api/person/v1", "POST");
        assertHasLink(personSeven, "update", "/api/person/v1", "PUT");
        assertHasLink(personSeven, "delete", "/api/person/v1/7", "DELETE");
    }

    private static void assertPerson(PersonDTO person, int index) {
        assertNotNull(person);
        assertNotNull(person.getId());
        assertNotNull(person.getLinks());

        assertEquals("First Name Test" + index, person.getFirstName());
        assertEquals("Last Name Test" + index, person.getLastName());
        assertEquals("Address Test" + index, person.getAddress());
        assertEquals(index % 2 == 0 ? "Male" : "Female", person.getGender());
        assertEquals(index % 2 == 0, person.getEnabled());
        assertEquals("Profile Url Test" + index, person.getProfileUrl());
        assertEquals("Photo Url Test" + index, person.getPhotoUrl());
    }

    private static void assertHasLink(PersonDTO dto, String rel, String href, String httpMethod) {
        assertTrue(dto.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals(rel)
                        && link.getHref().contains(href)
                        && link.getType().equals(httpMethod)
                )
        );
    }
}