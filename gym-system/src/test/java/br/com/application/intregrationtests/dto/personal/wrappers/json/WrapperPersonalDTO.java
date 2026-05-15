package br.com.application.intregrationtests.dto.personal.wrappers.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class WrapperPersonalDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("_embedded")
    private PersonalEmbeddedDTO embedded;

    public WrapperPersonalDTO() {}

    public PersonalEmbeddedDTO getEmbedded() {
        return embedded;
    }

    public void setEmbedded(PersonalEmbeddedDTO embedded) {
        this.embedded = embedded;
    }
}
