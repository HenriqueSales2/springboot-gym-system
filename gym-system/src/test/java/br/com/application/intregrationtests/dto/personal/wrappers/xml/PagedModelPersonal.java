package br.com.application.intregrationtests.dto.personal.wrappers.xml;

import br.com.application.intregrationtests.dto.person.PersonDTO;
import br.com.application.intregrationtests.dto.personal.PersonalDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.List;

@XmlRootElement
public class PagedModelPersonal implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement( name = "content")
    public List<PersonalDTO> content;

    public PagedModelPersonal() {}

    public List<PersonalDTO> getContent() {
        return content;
    }

    public void setContent(List<PersonalDTO> content) {
        this.content = content;
    }
}
