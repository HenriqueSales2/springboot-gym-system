package br.com.application.intregrationtests.dto.workout.wrappers.xml;

import br.com.application.intregrationtests.dto.workout.WorkoutDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.List;

@XmlRootElement
public class PagedModelWorkout implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement( name = "content")
    public List<WorkoutDTO> content;

    public PagedModelWorkout() {}

    public List<WorkoutDTO> getContent() {
        return content;
    }

    public void setContent(List<WorkoutDTO> content) {
        this.content = content;
    }
}
