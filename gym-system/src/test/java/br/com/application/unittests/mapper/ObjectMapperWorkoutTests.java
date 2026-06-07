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

        assertEquals(Long.valueOf(0L), output.getId());

        assertEquals("Exercise Name Test0", output.getExerciseName());
        assertEquals("Muscle Group Test0", output.getMuscleGroup());
        assertEquals("Equipment Test0", output.getEquipment());
        assertEquals("Difficulty Test0", output.getDifficulty());
    }

    @Test
    public void parseEntityListToDTOListTest() {
        List<WorkoutDTO> outputList = parseListObjects(inputObject.mockEntityList(), WorkoutDTO.class);

        WorkoutDTO outputZero = outputList.get(0);

        assertEquals(Long.valueOf(0L), outputZero.getId());
        assertEquals("Exercise Name Test0", outputZero.getExerciseName());
        assertEquals("Muscle Group Test0", outputZero.getMuscleGroup());
        assertEquals("Equipment Test0", outputZero.getEquipment());
        assertEquals("Difficulty Test0", outputZero.getDifficulty());

        WorkoutDTO outputSeven = outputList.get(7);

        assertEquals(Long.valueOf(7L), outputSeven.getId());
        assertEquals("Exercise Name Test7", outputSeven.getExerciseName());
        assertEquals("Muscle Group Test7", outputSeven.getMuscleGroup());
        assertEquals("Equipment Test7", outputSeven.getEquipment());
        assertEquals("Difficulty Test7", outputSeven.getDifficulty());

        WorkoutDTO outputTwelve = outputList.get(12);

        assertEquals(Long.valueOf(12L), outputTwelve.getId());
        assertEquals("Exercise Name Test12", outputTwelve.getExerciseName());
        assertEquals("Muscle Group Test12", outputTwelve.getMuscleGroup());
        assertEquals("Equipment Test12", outputTwelve.getEquipment());
        assertEquals("Difficulty Test12", outputTwelve.getDifficulty());
    }

    @Test
    public void parseDTOToEntityTest() {
        Workout output = parseObject(inputObject.mockDTO(), Workout.class);

        assertEquals(Long.valueOf(0L), output.getId());

        assertEquals("Exercise Name Test0", output.getExerciseName());
        assertEquals("Muscle Group Test0", output.getMuscleGroup());
        assertEquals("Equipment Test0", output.getEquipment());
        assertEquals("Difficulty Test0", output.getDifficulty());
    }

    @Test
    public void parserDTOListToEntityListTest() {
        List<Workout> outputList = parseListObjects(inputObject.mockDTOList(), Workout.class);

        Workout outputZero = outputList.get(0);

        assertEquals(Long.valueOf(0L), outputZero.getId());

        assertEquals("Exercise Name Test0", outputZero.getExerciseName());
        assertEquals("Muscle Group Test0", outputZero.getMuscleGroup());
        assertEquals("Equipment Test0", outputZero.getEquipment());
        assertEquals("Difficulty Test0", outputZero.getDifficulty());

        Workout outputSeven = outputList.get(7);

        assertEquals(Long.valueOf(7L), outputSeven.getId());
        assertEquals("Exercise Name Test7", outputSeven.getExerciseName());
        assertEquals("Muscle Group Test7", outputSeven.getMuscleGroup());
        assertEquals("Equipment Test7", outputSeven.getEquipment());
        assertEquals("Difficulty Test7", outputSeven.getDifficulty());

        Workout outputTwelve = outputList.get(12);

        assertEquals(Long.valueOf(12L), outputTwelve.getId());
        assertEquals("Exercise Name Test12", outputTwelve.getExerciseName());
        assertEquals("Muscle Group Test12", outputTwelve.getMuscleGroup());
        assertEquals("Equipment Test12", outputTwelve.getEquipment());
        assertEquals("Difficulty Test12", outputTwelve.getDifficulty());
    }
}