package br.com.application.intregrationtests.serialization.converter;

import br.com.application.intregrationtests.dto.withyaml.PersonDTOYaml;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;


public final class ConvertYAML {

    public static RequestSpecification specification;
    public static ObjectMapper mapper;
    public static PersonDTOYaml personDTO;

    private ConvertYAML() throws JsonProcessingException {
        mapper = new ObjectMapper(new YAMLFactory());
        String yamlBody = mapper.writeValueAsString(personDTO);
    }
}

