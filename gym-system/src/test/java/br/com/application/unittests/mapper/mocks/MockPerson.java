package br.com.application.unittests.mapper.mocks;

import java.util.ArrayList;
import java.util.List;

import br.com.application.data.dto.PersonDTO;
import br.com.application.model.Person;

public class MockPerson {


    public Person mockEntity() {
        return mockEntity(0);
    }
    
    public PersonDTO mockDTO() {
        return mockDTO(0);
    }
    
    public List<Person> mockEntityList() {
        List<Person> persons = new ArrayList<Person>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockEntity(i));
        }
        return persons;
    }

    public List<PersonDTO> mockDTOList() {
        List<PersonDTO> persons = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockDTO(i));
        }
        return persons;
    }
    
    public Person mockEntity(Integer number) {
        Person person = new Person();
        person.setId(number.longValue());
        person.setFirstName("First Name Test" + number);
        person.setLastName("Last Name Test" + number);
        person.setAddress("Address Test" + number);
        person.setGender(((number % 2)==0) ? "Male" : "Female");
        return person;
    }

    public PersonDTO mockDTO(Integer number) {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setId(number.longValue());
        personDTO.setFirstName("First Name Test" + number);
        personDTO.setLastName("Last Name Test" + number);
        personDTO.setAddress("Address Test" + number);
        personDTO.setGender(((number % 2)==0) ? "Male" : "Female");
        return personDTO;
    }

}