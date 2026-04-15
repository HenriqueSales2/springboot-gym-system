package br.com.application.service;

import br.com.application.controllers.personal.PersonalController;
import br.com.application.controllers.person.TestLogController;
import br.com.application.data.dto.PersonalDTO;
import br.com.application.exception.RequiredObjectIsNullException;
import br.com.application.exception.ResourceNotFoundException;
import br.com.application.model.Personal;
import br.com.application.repository.PersonalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.application.mapper.ObjectMapper.parseListObjects;
import static br.com.application.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service // serve para injetar dependências sem ficar dando "new Objeto" por exemplo
public class PersonalService {

    private Logger logger = LoggerFactory.getLogger(TestLogController.class.getName()); // logar informações importantes do projeto

    @Autowired
    PersonalRepository repository;


    public List<PersonalDTO> findAllTeachers() {
        logger.info("Finding all People!");
        var teachers = parseListObjects(repository.findAll(), PersonalDTO.class); // retornando uma lista de entidades
        teachers.forEach(PersonalService::addHateoasLinks); // adicionando método Reference, ideal para listas
        return teachers;
    }

    public PersonalDTO findPersonalById(Long id) {
        logger.info("Finding one Personal!");
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No record found for this ID!"));
        var dto = parseObject(entity, PersonalDTO.class);
        addHateoasLinks(dto);
        return dto;
    }


    public PersonalDTO create(PersonalDTO personalDTO) {

        if (personalDTO == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one Person!");
        var entity = parseObject(personalDTO, Personal.class); // fazendo a conversão de PersonalDTO para Personal
        var dto = parseObject(repository.save(entity), PersonalDTO.class); // convertendo em DTO, criando, e salvando a entidade
        addHateoasLinks(dto);
        return dto;
    }

    public PersonalDTO update(PersonalDTO personalDTO) {

        if (personalDTO == null) throw new RequiredObjectIsNullException();

        logger.info("Updating one Person!");
        Personal personal = repository.findById(personalDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No record found for this ID!"));
        personal.setFirstName(personalDTO.getFirstName());
        personal.setLastName(personalDTO.getLastName());
        personal.setAddress(personalDTO.getAddress());
        personal.setGender(personalDTO.getGender());

        var entity = repository.save(personal);
        var dto = parseObject(entity, PersonalDTO.class); // convertendo em DTO, atualizando, e salvando a entidade
        addHateoasLinks(dto);
        return dto;
    }

    public void delete(Long id) {
        logger.info("Delete one Person!");
        Personal entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No record found for this ID!"));
        repository.delete(entity);
    }

    private static void addHateoasLinks(PersonalDTO dto) {
        dto.add(linkTo(methodOn(PersonalController.class)
                .findPersonalById(dto.getId())) // passando o id como parâmetro
                .withSelfRel() // redirecionando a URL onde acontecerá a ocorrência
                .withType("GET")); // tipo de método HTTP

        dto.add(linkTo(methodOn(PersonalController.class)
                .findAllTeachers()) // sem parâmetros
                .withRel("findAll") // passamos o relacionamento dentro dos parenteses, nesse caso é o findAll
                .withType("GET")); // tipo de método HTTP

        dto.add(linkTo(methodOn(PersonalController.class)
                .create(dto)) // passando o PersonalDTO como parâmetro
                .withRel("create") // passamos o relacionamento dentro dos parenteses, nesse caso é o create
                .withType("POST")); // tipo de método HTTP

        dto.add(linkTo(methodOn(PersonalController.class)
                .update(dto)) // passando o PersonalDTO como parâmetro
                .withRel("update") // passamos o relacionamento dentro dos parenteses, nesse caso é o update
                .withType("PUT")); // tipo de método HTTP


        dto.add(linkTo(methodOn(PersonalController.class)
                .delete(dto.getId())) // passando o id como parâmetro
                .withRel("delete") // passamos o relacionamento dentro dos parenteses, nesse caso é o delete
                .withType("DELETE")); // tipo de método HTTP
    }
}
