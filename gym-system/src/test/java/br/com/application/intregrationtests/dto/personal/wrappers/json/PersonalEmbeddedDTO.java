package br.com.application.intregrationtests.dto.personal.wrappers.json;

import br.com.application.intregrationtests.dto.personal.PersonalDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class PersonalEmbeddedDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("personals")
    private List<PersonalDTO> personals;

    public PersonalEmbeddedDTO() {}

    public List<PersonalDTO> getPersonals() {
        return personals;
    }

    public void setPersonals(List<PersonalDTO> personals) {
        this.personals = personals;
    }
}
