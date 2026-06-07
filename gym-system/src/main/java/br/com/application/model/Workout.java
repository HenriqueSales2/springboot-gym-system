package br.com.application.model;

import jakarta.persistence.*;


import java.util.Objects;

@Entity
@Table(name = "workouts")
public class Workout {

    private static final long serialVersionUID = 1L;

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column(name = "exercise_name", nullable = false, length = 100)
private String exerciseName;

@Column(name = "muscle_group", nullable = false, length = 50)
private String muscleGroup;

@Column(nullable = false, length = 50)
private String equipment;

@Column(nullable = false, length = 20)
private String difficulty;

    public Workout() {}

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
        Workout workout = (Workout) o;
        return Objects.equals(id, workout.id) && Objects.equals(exerciseName, workout.exerciseName) && Objects.equals(muscleGroup, workout.muscleGroup) && Objects.equals(equipment, workout.equipment) && Objects.equals(difficulty, workout.difficulty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, exerciseName, muscleGroup, equipment, difficulty);
    }
}
