package br.com.application.intregrationtests.dto.workout.wrappers.json;

import br.com.application.intregrationtests.dto.workout.WorkoutDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class WorkoutEmbeddedDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("workouts")
    private List<WorkoutDTO> workouts;

    public WorkoutEmbeddedDTO() {}

    public List<WorkoutDTO> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(List<WorkoutDTO> workouts) {
        this.workouts = workouts;
    }
}
