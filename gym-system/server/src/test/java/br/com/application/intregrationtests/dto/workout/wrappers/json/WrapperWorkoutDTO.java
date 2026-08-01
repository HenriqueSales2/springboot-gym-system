package br.com.application.intregrationtests.dto.workout.wrappers.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class WrapperWorkoutDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("_embedded")
    private WorkoutEmbeddedDTO embedded;

    public WrapperWorkoutDTO() {}

    public WorkoutEmbeddedDTO getEmbedded() {
        return embedded;
    }

    public void setEmbedded(WorkoutEmbeddedDTO embedded) {
        this.embedded = embedded;
    }
}