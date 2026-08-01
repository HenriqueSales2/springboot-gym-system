package br.com.application.unittests.mapper;

import br.com.application.data.dto.WorkoutDTO;
import br.com.application.model.Workout;
import br.com.application.unittests.mapper.mocks.MockWorkout;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static br.com.application.mapper.ObjectMapper.parseListObjects;
import static br.com.application.mapper.ObjectMapper.parseObject;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ObjectMapperWorkoutTests {
    MockWorkout inputObject;

    @BeforeEach
    public void setUp() {
        inputObject = new MockWorkout();
    }

    @Test
    public void parseEntityToDTOTest() {
        WorkoutDTO output = parseObject(inputObject.mockEntity(), WorkoutDTO.class);
        assertWorkoutDTO(output, 0);
    }

    @Test
    public void parseEntityListToDTOListTest() {
        List<WorkoutDTO> outputList = parseListObjects(inputObject.mockEntityList(), WorkoutDTO.class);

        WorkoutDTO outputZero = outputList.get(0);
        assertWorkoutDTO(outputZero, 0);

        WorkoutDTO outputSeven = outputList.get(7);
        assertWorkoutDTO(outputSeven, 7);

        WorkoutDTO outputTwelve = outputList.get(12);
        assertWorkoutDTO(outputTwelve, 12);
    }

    @Test
    public void parseDTOToEntityTest() {
        Workout output = parseObject(inputObject.mockDTO(), Workout.class);
        assertWorkout(output, 0);
    }

    @Test
    public void parserDTOListToEntityListTest() {
        List<Workout> outputList = parseListObjects(inputObject.mockDTOList(), Workout.class);

        Workout outputZero = outputList.get(0);
        assertWorkout(outputZero, 0);

        Workout outputSeven = outputList.get(7);
        assertWorkout(outputSeven, 7);

        Workout outputTwelve = outputList.get(12);
        assertWorkout(outputTwelve, 12);
    }

    private void assertWorkoutDTO(WorkoutDTO workout, int index) {
        assertEquals(Long.valueOf(index), workout.getId());
        assertEquals("Exercise Name Test" + index, workout.getExerciseName());
        assertEquals("Muscle Group Test" + index, workout.getMuscleGroup());
        assertEquals("Equipment Test" + index, workout.getEquipment());
        assertEquals("Difficulty Test" + index, workout.getDifficulty());
    }

    private void assertWorkout(Workout workout, int index) {
        assertEquals(Long.valueOf(index), workout.getId());
        assertEquals("Exercise Name Test" + index, workout.getExerciseName());
        assertEquals("Muscle Group Test" + index, workout.getMuscleGroup());
        assertEquals("Equipment Test" + index, workout.getEquipment());
        assertEquals("Difficulty Test" + index, workout.getDifficulty());
    }
}