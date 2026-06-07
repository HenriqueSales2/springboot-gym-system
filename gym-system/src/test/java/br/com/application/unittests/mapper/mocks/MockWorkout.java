package br.com.application.unittests.mapper.mocks;

import br.com.application.data.dto.WorkoutDTO;
import br.com.application.model.Workout;

import java.util.ArrayList;
import java.util.List;

public class MockWorkout {

    public Workout mockEntity() {
        return mockEntity(0);
    }

    public WorkoutDTO mockDTO() {
        return mockDTO(0);
    }

    public List<Workout> mockEntityList() {
        List<Workout> workouts = new ArrayList<Workout>();
        for (int i = 0; i < 14; i++) {
            workouts.add(mockEntity(i));
        }
        return workouts;
    }

    public List<WorkoutDTO> mockDTOList() {
        List<WorkoutDTO> workouts = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            workouts.add(mockDTO(i));
        }
        return workouts;
    }

    public Workout mockEntity(Integer number) {
        Workout workout = new Workout();
        workout.setId(number.longValue());
        workout.setExerciseName("Exercise Name Test" + number);
        workout.setMuscleGroup("Muscle Group Test" + number);
        workout.setEquipment("Equipment Test" + number);
        workout.setDifficulty("Difficulty Test" + number);
        return workout;
    }

    public WorkoutDTO mockDTO(Integer number) {
        WorkoutDTO workoutDTO = new WorkoutDTO();
        workoutDTO.setId(number.longValue());
        workoutDTO.setExerciseName("Exercise Name Test" + number);
        workoutDTO.setMuscleGroup("Muscle Group Test" + number);
        workoutDTO.setEquipment("Equipment Test" + number);
        workoutDTO.setDifficulty("Difficulty Test" + number);
        return workoutDTO;
    }
}
