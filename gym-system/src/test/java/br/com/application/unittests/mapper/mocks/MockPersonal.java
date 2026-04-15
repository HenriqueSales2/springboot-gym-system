package br.com.application.unittests.mapper.mocks;

import br.com.application.data.dto.PersonalDTO;
import br.com.application.model.Person;
import br.com.application.model.Personal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockPersonal {

    public Personal mockEntity() {
        return mockEntity(0);
    }

    public PersonalDTO mockDTO() {
        return mockDTO(0);
    }

    public List<Personal> mockEntityList() {
        List<Personal> teachers = new ArrayList<Personal>();
        for (int i = 0; i < 14; i++) {
            teachers.add(mockEntity(i));
        }
        return teachers;
    }

    public List<PersonalDTO> mockDTOList() {
        List<PersonalDTO> teachers = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            teachers.add(mockDTO(i));
        }
        return teachers;
    }

    public Personal mockEntity(Integer number) {
        Personal personal = new Personal();
        personal.setAddress("Address Test" + number);
        personal.setFirstName("First Name Test" + number);
        personal.setGender(((number % 2)==0) ? "Male" : "Female");
        personal.setId(number.longValue());
        personal.setLastName("Last Name Test" + number);
        return personal;
    }

    public PersonalDTO mockDTO(Integer number) {
        PersonalDTO personal = new PersonalDTO();
        personal.setAddress("Address Test" + number);
        personal.setFirstName("First Name Test" + number);
        personal.setGender(((number % 2)==0) ? "Male" : "Female");
        personal.setId(number.longValue());
        personal.setLastName("Last Name Test" + number);
        return personal;
    }
}
