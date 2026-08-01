package br.com.application.data.dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.io.Serializable;
import java.util.Objects;

@Relation(collectionRelation = "workouts")
public class WorkoutDTO extends RepresentationModel<WorkoutDTO> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String exerciseName;
    private String muscleGroup;
    private String equipment;
    private String difficulty;

    public WorkoutDTO(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        WorkoutDTO that = (WorkoutDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(exerciseName, that.exerciseName) && Objects.equals(muscleGroup, that.muscleGroup) && Objects.equals(equipment, that.equipment) && Objects.equals(difficulty, that.difficulty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, exerciseName, muscleGroup, equipment, difficulty);
    }
}