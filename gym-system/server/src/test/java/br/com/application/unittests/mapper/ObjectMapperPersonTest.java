package br.com.application.unittests.mapper;

import static br.com.application.mapper.ObjectMapper.parseListObjects;
import static br.com.application.mapper.ObjectMapper.parseObject;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import br.com.application.data.dto.PersonDTO;
import br.com.application.model.Person;
import br.com.application.unittests.mapper.mocks.MockPerson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ObjectMapperPersonTest {
    MockPerson inputObject;

    @BeforeEach
    public void setUp() {
        inputObject = new MockPerson();
    }

    @Test
    public void parseEntityToDTOTest() {
        PersonDTO output = parseObject(inputObject.mockEntity(), PersonDTO.class);
        assertPersonDTO(output, 0);
    }

    @Test
    public void parseEntityListToDTOListTest() {
        List<PersonDTO> outputList = parseListObjects(inputObject.mockEntityList(), PersonDTO.class);

        PersonDTO outputZero = outputList.get(0);
        assertPersonDTO(outputZero, 0);

        PersonDTO outputSeven = outputList.get(7);
        assertPersonDTO(outputSeven, 7);

        PersonDTO outputTwelve = outputList.get(12);
        assertPersonDTO(outputTwelve, 12);
    }

    @Test
    public void parseDTOToEntityTest() {
        Person output = parseObject(inputObject.mockDTO(), Person.class);
        assertPerson(output, 0);
    }

    @Test
    public void parserDTOListToEntityListTest() {
        List<Person> outputList = parseListObjects(inputObject.mockDTOList(), Person.class);

        Person outputZero = outputList.get(0);
        assertPerson(outputZero, 0);

        Person outputSeven = outputList.get(7);
        assertPerson(outputSeven, 7);

        Person outputTwelve = outputList.get(12);
        assertPerson(outputTwelve, 12);
    }

    private void assertPersonDTO(PersonDTO person, int index) {
        assertEquals(Long.valueOf(index), person.getId());
        assertEquals("First Name Test" + index, person.getFirstName());
        assertEquals("Last Name Test" + index, person.getLastName());
        assertEquals("Address Test" + index, person.getAddress());
        assertEquals(index % 2 == 0 ? "Male" : "Female", person.getGender());
        assertEquals(index % 2 == 0, person.getEnabled());
        assertEquals("Profile Url Test" + index, person.getProfileUrl());
        assertEquals("Photo Url Test" + index, person.getPhotoUrl());
    }

    private void assertPerson(Person person, int index) {
        assertEquals(Long.valueOf(index), person.getId());
        assertEquals("First Name Test" + index, person.getFirstName());
        assertEquals("Last Name Test" + index, person.getLastName());
        assertEquals("Address Test" + index, person.getAddress());
        assertEquals(index % 2 == 0 ? "Male" : "Female", person.getGender());
        assertEquals(index % 2 == 0, person.getEnabled());
        assertEquals("Profile Url Test" + index, person.getProfileUrl());
        assertEquals("Photo Url Test" + index, person.getPhotoUrl());
    }
}