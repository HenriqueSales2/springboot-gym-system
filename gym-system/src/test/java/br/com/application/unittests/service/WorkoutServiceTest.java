package br.com.application.unittests.service;

import br.com.application.data.dto.WorkoutDTO;
import br.com.application.exception.RequiredObjectIsNullException;
import br.com.application.model.Workout;
import br.com.application.repository.WorkoutRepository;
import br.com.application.service.WorkoutService;
import br.com.application.unittests.mapper.mocks.MockWorkout;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) // o ciclo de vida dos objetos vão durar apenas para essa classe
@ExtendWith(MockitoExtension.class)
class WorkoutServiceTest {

    MockWorkout input; // vamos passar como parâmetro para os testes

    @InjectMocks
    private WorkoutService service;

    @Mock
    WorkoutRepository repository;

    @BeforeEach
    void setUp() {
        input = new MockWorkout();
        MockitoAnnotations.openMocks(this); // essa linha é muito importante, pois sem isso, é inviável injetar o Repository e Service na classe de testes
    }

    @Test
    void findWorkoutById() { // testa se todos os campos do Objeto Person e os Links estão funcionando, caso não estejam, retorna um erro
        Workout workout =  input.mockEntity(1);
        workout.setId(1L);
        when(repository // dizendo ao Mockito o que ele deve fazer quando o Repositório for invocado
                .findById(1L)) // nesse caso é chamar o método findById (passando como parâmetro o id, que seria um mock)
                .thenReturn(Optional.of(workout)); // e o que ele deve retornar (ele vai retornar uma instancia de "person")

        var result = service.findWorkoutById(1L);

        assertNotNull(result); // verificando se os objetos são coerentes
        assertNotNull(result.getId()); // verificando se os id é coerente
        assertNotNull(result.getLinks()); // verificando se o link é coerente
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self") // verificando se a propriedade "self" é coerente em ambas as partes (self é o findById)
                                && link.getHref().endsWith("/api/workout/v1/1") // verificando se a propriedade "href" é coerente em ambas as partes
                                && link.getType().equals("GET") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAllWorkouts") // verificando se a propriedade "findAll" é coerente em ambas as partes
                                && link.getHref().endsWith("/api/workout/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                                && link.getType().equals("GET") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create") // verificando se a propriedade "create" é coerente em ambas as partes
                                && link.getHref().endsWith("/api/workout/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                                && link.getType().equals("POST") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update") // verificando se a propriedade "update" é coerente em ambas as partes
                                && link.getHref().endsWith("/api/workout/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                                && link.getType().equals("PUT") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete") // verificando se a propriedade "delete" é coerente em ambas as partes
                                && link.getHref().endsWith("/api/workout/v1/1") // verificando se a propriedade "href" é coerente em ambas as partes
                                && link.getType().equals("DELETE") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );

        assertEquals("Exercise Name Test1", result.getExerciseName());
        assertEquals("Muscle Group Test1", result.getMuscleGroup());
        assertEquals("Equipment Test1", result.getEquipment());
        assertEquals("Difficulty Test1", result.getDifficulty());


    }

    @Test
    void create() {

        WorkoutDTO dto = input.mockDTO(1);

        when(repository // dizendo ao Mockito o que ele deve fazer quando o Repositório for invocado
                .save(any(Workout.class))) // nesse caso é chamar o método findById (passando como parâmetro o id, que seria um mock)
                .thenReturn(input.mockEntity(1)); // e o que ele deve retornar (ele vai retornar uma instancia de "person", dessa vez não é Optional)

        var result = service.create(dto);

        assertNotNull(result); // verificando se os objetos são coerentes
        assertNotNull(result.getId()); // verificando se os id é coerente
        assertNotNull(result.getLinks()); // verificando se o link é coerente
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self") // verificando se a propriedade "self" é coerente em ambas as partes (self é o findById)
                        && link.getHref().endsWith("/api/workout/v1/1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("GET") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAllWorkouts") // verificando se a propriedade "findAll" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/workout/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("GET") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create") // verificando se a propriedade "create" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/workout/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("POST") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update") // verificando se a propriedade "update" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/workout/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("PUT") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete") // verificando se a propriedade "delete" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/workout/v1/1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("DELETE") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );

        assertEquals("Exercise Name Test1", result.getExerciseName());
        assertEquals("Muscle Group Test1", result.getMuscleGroup());
        assertEquals("Equipment Test1", result.getEquipment());
        assertEquals("Difficulty Test1", result.getDifficulty());


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

        Workout workout =  input.mockEntity(1);
        Workout persisted = workout;
        persisted.setId(1L);

        WorkoutDTO dto = input.mockDTO(1);

        workout.setId(1L);
        when(repository // dizendo ao Mockito o que ele deve fazer quando o Repositório for invocado
                .findById(1L)) // nesse caso é chamar o método findById (passando como parâmetro o id, que seria um mock)
                .thenReturn(Optional.of(workout));
        when(repository.save(workout)).thenReturn(persisted);
        var result = service.update(dto);

        assertNotNull(result); // verificando se os objetos são coerentes
        assertNotNull(result.getId()); // verificando se os id é coerente
        assertNotNull(result.getLinks()); // verificando se o link é coerente
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self") // verificando se a propriedade "self" é coerente em ambas as partes (self é o findById)
                        && link.getHref().endsWith("/api/workout/v1/1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("GET") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAllWorkouts") // verificando se a propriedade "findAll" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/workout/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("GET") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create") // verificando se a propriedade "create" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/workout/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("POST") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update") // verificando se a propriedade "update" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/workout/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("PUT") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete") // verificando se a propriedade "delete" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/workout/v1/1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("DELETE") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );

        assertEquals("Exercise Name Test1", result.getExerciseName());
        assertEquals("Muscle Group Test1", result.getMuscleGroup());
        assertEquals("Equipment Test1", result.getEquipment());
        assertEquals("Difficulty Test1", result.getDifficulty());

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
        Workout workout = input.mockEntity(1);
        workout.setId(1L);
        when(repository // dizendo ao Mockito o que ele deve fazer quando o Repositório for invocado
                .findById(1L)) // nesse caso é chamar o método findById (passando como parâmetro o id, que seria um mock)
                .thenReturn(Optional.of(workout)); // e o que ele deve retornar (ele vai retornar uma instancia de "person")

        service.delete(1L);
        verify(repository, times(1)).findById(anyLong()); // vai invocar o número de vezes que eu chamar o método FindById
        verify(repository, times(1)).delete(any(Workout.class)); // vai invocar o número de vezes que eu chamar o método delete passando como parâmetro a entidade Person
        verifyNoMoreInteractions(repository);
    }

    @Test
    @Disabled("REASON: Still Under Development")
    void findAll() {

        List<Workout> list = input.mockEntityList();
        when(repository.findAll()).thenReturn(list);
        List<WorkoutDTO> workouts =  new ArrayList<>();//service.findAllTeachers();

        assertEquals(14, workouts.size());

        var workoutOne = workouts.get(1);

        assertNotNull(workoutOne); // verificando se os objetos são coerentes
        assertNotNull(workoutOne.getId()); // verificando se os id é coerente
        assertNotNull(workoutOne.getLinks()); // verificando se o link é coerente
        assertNotNull(workoutOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self") // verificando se a propriedade "self" é coerente em ambas as partes (self é o findById)
                        && link.getHref().endsWith("/api/workout/v1/1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("GET") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(workoutOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAllWorkouts") // verificando se a propriedade "findAll" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/workout/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("GET") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(workoutOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create") // verificando se a propriedade "create" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/workout/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("POST") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(workoutOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update") // verificando se a propriedade "update" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/workout/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("PUT") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(workoutOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete") // verificando se a propriedade "delete" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/workout/v1/1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("DELETE") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );

        assertEquals("Exercise Name Test1", workoutOne.getExerciseName());
        assertEquals("Muscle Group Test1", workoutOne.getMuscleGroup());
        assertEquals("Equipment Test1", workoutOne.getEquipment());
        assertEquals("Difficulty Test1", workoutOne.getDifficulty());


        var workoutFour = workouts.get(4);

        assertNotNull(workoutFour); // verificando se os objetos são coerentes
        assertNotNull(workoutFour.getId()); // verificando se os id é coerente
        assertNotNull(workoutFour.getLinks()); // verificando se o link é coerente
        assertNotNull(workoutFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self") // verificando se a propriedade "self" é coerente em ambas as partes (self é o findById)
                        && link.getHref().endsWith("/api/workout/v1/4") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("GET") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(workoutFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAllWorkouts") // verificando se a propriedade "findAll" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/workout/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("GET") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(workoutFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create") // verificando se a propriedade "create" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/workout/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("POST") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(workoutFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update") // verificando se a propriedade "update" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/workout/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("PUT") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(workoutFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete") // verificando se a propriedade "delete" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/workout/v1/4") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("DELETE") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );

        assertEquals("Exercise Name Test4", workoutFour.getExerciseName());
        assertEquals("Muscle Group Test4", workoutFour.getMuscleGroup());
        assertEquals("Equipment Test4", workoutFour.getEquipment());
        assertEquals("Difficulty Test4", workoutFour.getDifficulty());


        var workoutSeven = workouts.get(7);

        assertNotNull(workoutSeven); // verificando se os objetos são coerentes
        assertNotNull(workoutSeven.getId()); // verificando se os id é coerente
        assertNotNull(workoutSeven.getLinks()); // verificando se o link é coerente
        assertNotNull(workoutSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self") // verificando se a propriedade "self" é coerente em ambas as partes (self é o findById)
                        && link.getHref().endsWith("/api/workout/v1/7") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("GET") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(workoutSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAllWorkouts") // verificando se a propriedade "findAll" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/workout/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("GET") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(workoutSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create") // verificando se a propriedade "create" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/workout/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("POST") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(workoutSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update") // verificando se a propriedade "update" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/workout/v1") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("PUT") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );


        assertNotNull(workoutSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete") // verificando se a propriedade "delete" é coerente em ambas as partes
                        && link.getHref().endsWith("/api/workout/v1/7") // verificando se a propriedade "href" é coerente em ambas as partes
                        && link.getType().equals("DELETE") // verificando se a propriedade "type" é coerente em ambas as partes
                )
        );

        assertEquals("Exercise Name Test7", workoutSeven.getExerciseName());
        assertEquals("Muscle Group Test7", workoutSeven.getMuscleGroup());
        assertEquals("Equipment Test7", workoutSeven.getEquipment());
        assertEquals("Difficulty Test7", workoutSeven.getDifficulty());

    }
}