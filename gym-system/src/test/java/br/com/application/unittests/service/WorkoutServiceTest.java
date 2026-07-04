package br.com.application.unittests.service;

import br.com.application.data.dto.WorkoutDTO;
import br.com.application.exception.RequiredObjectIsNullException;
import br.com.application.model.Workout;
import br.com.application.repository.WorkoutRepository;
import br.com.application.service.workout.WorkoutService;
import br.com.application.unittests.mapper.mocks.MockWorkout;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WorkoutServiceTest {

    MockWorkout input;

    @InjectMocks
    private WorkoutService service;

    @Mock
    WorkoutRepository repository;

    @Mock
    private PagedResourcesAssembler<WorkoutDTO> assembler;

    @BeforeEach
    void setUp() {
        input = new MockWorkout();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Order(1)
    void createWorkoutTest() {
        WorkoutDTO dto = input.mockDTO(1);

        when(repository
                .save(any(Workout.class)))
                .thenReturn(input.mockEntity(1));

        var result = service.create(dto);

        assertWorkout(result, 1);

        assertHasLink(result, "self", "/api/workout/v1/1", "GET");
        assertHasLink(result, "findAllWorkouts", "/api/workout/v1", "GET");
        assertHasLink(result, "create", "/api/workout/v1", "POST");
        assertHasLink(result, "update", "/api/workout/v1", "PUT");
        assertHasLink(result, "delete", "/api/workout/v1/1", "DELETE");
    }

    @Test
    @Order(2)
    void createWithNullWorkoutTest() {
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
    void updateWorkoutTest() {
        Workout workout =  input.mockEntity(1);
        Workout persisted = workout;
        persisted.setId(1L);

        WorkoutDTO dto = input.mockDTO(1);

        workout.setId(1L);
        when(repository
                .findById(1L))
                .thenReturn(Optional.of(workout));
        when(repository.save(workout)).thenReturn(persisted);
        var result = service.update(dto);

        assertWorkout(result, 1);

        assertHasLink(result, "self", "/api/workout/v1/1", "GET");
        assertHasLink(result, "findAllWorkouts", "/api/workout/v1", "GET");
        assertHasLink(result, "create", "/api/workout/v1", "POST");
        assertHasLink(result, "update", "/api/workout/v1", "PUT");
        assertHasLink(result, "delete", "/api/workout/v1/1", "DELETE");
    }

    @Test
    @Order(4)
    void updateWithNullWorkoutTest() {
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
    void findWorkoutByIdTest() {
        Workout workout =  input.mockEntity(1);
        workout.setId(1L);
        when(repository
                .findById(1L))
                .thenReturn(Optional.of(workout));

        var result = service.findWorkoutById(1L);

        assertWorkout(result, 1);

        assertHasLink(result, "self", "/api/workout/v1/1", "GET");
        assertHasLink(result, "findAllWorkouts", "/api/workout/v1", "GET");
        assertHasLink(result, "create", "/api/workout/v1", "POST");
        assertHasLink(result, "update", "/api/workout/v1", "PUT");
        assertHasLink(result, "delete", "/api/workout/v1/1", "DELETE");
    }

    @Test
    @Order(6)
    void deleteWorkoutTest() {
        Workout workout = input.mockEntity(1);
        workout.setId(1L);
        when(repository
                .findById(1L))
                .thenReturn(Optional.of(workout));

        service.delete(1L);
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(Workout.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    @Order(7)
    void findAllWorkoutsTest() {
        var page = 0;
        var size = 12;
        var direction = "asc";

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "exerciseName"));

        List<Workout> list = input.mockEntityList();

        Page<Workout> pageImpl = new PageImpl<>(list);

        PagedModel<EntityModel<WorkoutDTO>> pagedModel = mock(PagedModel.class);

        when(repository.findAll(any(Pageable.class)))
                .thenReturn(pageImpl);

        when(assembler.toModel(any(Page.class), any(Link.class)))
                .thenReturn(pagedModel);

        service.findAllWorkout(pageable);

        ArgumentCaptor<Page<WorkoutDTO>> pageCaptor = ArgumentCaptor.forClass(Page.class);

        verify(assembler).toModel(pageCaptor.capture(), any(Link.class));

        Page<WorkoutDTO> pageSentToAssembler = pageCaptor.getValue();

        assertEquals(14, pageSentToAssembler.getContent().size());

        WorkoutDTO workoutOne = pageSentToAssembler.getContent().get(1);

        assertWorkout(workoutOne, 1);

        assertHasLink(workoutOne, "self", "/api/workout/v1/1", "GET");
        assertHasLink(workoutOne, "findAllWorkouts", "/api/workout/v1", "GET");
        assertHasLink(workoutOne, "create", "/api/workout/v1", "POST");
        assertHasLink(workoutOne, "update", "/api/workout/v1", "PUT");
        assertHasLink(workoutOne, "delete", "/api/workout/v1/1", "DELETE");

        WorkoutDTO workoutFour = pageSentToAssembler.getContent().get(4);

        assertWorkout(workoutFour, 4);

        assertHasLink(workoutFour, "self", "/api/workout/v1/4", "GET");
        assertHasLink(workoutFour, "findAllWorkouts", "/api/workout/v1", "GET");
        assertHasLink(workoutFour, "create", "/api/workout/v1", "POST");
        assertHasLink(workoutFour, "update", "/api/workout/v1", "PUT");
        assertHasLink(workoutFour, "delete", "/api/workout/v1/4", "DELETE");

        WorkoutDTO workoutSeven = pageSentToAssembler.getContent().get(7);

        assertWorkout(workoutSeven, 7);

        assertHasLink(workoutSeven, "self", "/api/workout/v1/7", "GET");
        assertHasLink(workoutSeven, "findAllWorkouts", "/api/workout/v1", "GET");
        assertHasLink(workoutSeven, "create", "/api/workout/v1", "POST");
        assertHasLink(workoutSeven, "update", "/api/workout/v1", "PUT");
        assertHasLink(workoutSeven, "delete", "/api/workout/v1/7", "DELETE");
    }

    private static void assertWorkout(WorkoutDTO workout, int index) {
        assertNotNull(workout);
        assertNotNull(workout.getId());
        assertNotNull(workout.getLinks());

        assertEquals("Exercise Name Test" + index, workout.getExerciseName());
        assertEquals("Muscle Group Test" + index, workout.getMuscleGroup());
        assertEquals("Equipment Test" + index, workout.getEquipment());
        assertEquals("Difficulty Test" + index, workout.getDifficulty());
    }

    private static void assertHasLink(WorkoutDTO dto, String rel, String href, String httpMethod) {
        assertTrue(dto.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals(rel)
                        && link.getHref().contains(href)
                        && link.getType().equals(httpMethod)
                )
        );
    }
}