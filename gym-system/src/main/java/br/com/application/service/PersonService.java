package br.com.application.service;

import br.com.application.controllers.person.PersonController;
import br.com.application.controllers.person.TestLogController;
import br.com.application.data.dto.PersonDTO;
import br.com.application.exception.RequiredObjectIsNullException;
import br.com.application.exception.ResourceNotFoundException;
import static br.com.application.mapper.ObjectMapper.parseObject;
import static br.com.application.mapper.ObjectMapper.parseListObjects;
import br.com.application.model.Person;
import br.com.application.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.stereotype.Service;

import java.util.List;

@Service // serve para injetar dependências sem ficar dando "new Objeto" por exemplo
public class PersonService {

    private Logger logger = LoggerFactory.getLogger(TestLogController.class.getName()); // logar informações importantes do projeto

    @Autowired
    PersonRepository repository;


    public List<PersonDTO> findAll() {
        logger.info("Finding all People!");
        var persons = parseListObjects(repository.findAll(), PersonDTO.class); // retornando uma lista de entidades
        persons.forEach(PersonService::addHateoasLinks); // adicionando método Reference, ideal para listas
        return persons;
    }

    public PersonDTO findById(Long id) {
        logger.info("Finding one Person!");
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No record found for this ID!"));
        var dto = parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }


    public PersonDTO create(PersonDTO personDTO) {

        if (personDTO == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one Person!");
        var entity = parseObject(personDTO, Person.class); // fazendo a conversão de PersonDTO para Person
        var dto = parseObject(repository.save(entity), PersonDTO.class); // convertendo em DTO, criando, e salvando a entidade
        addHateoasLinks(dto);
        return dto;
    }

    public PersonDTO update(PersonDTO personDTO) {

        if (personDTO == null) throw new RequiredObjectIsNullException();

        logger.info("Updating one Person!");
        Person person = repository.findById(personDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No record found for this ID!"));
        person.setFirstName(personDTO.getFirstName());
        person.setLastName(personDTO.getLastName());
        person.setAddress(personDTO.getAddress());
        person.setGender(personDTO.getGender());

        var entity = repository.save(person);
        var dto = parseObject(entity, PersonDTO.class); // convertendo em DTO, atualizando, e salvando a entidade
        addHateoasLinks(dto);
        return dto;
    }

    public void delete(Long id) {
        logger.info("Delete one Person!");
        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No record found for this ID!"));
        repository.delete(entity);
    }

    private static void addHateoasLinks(PersonDTO dto) {
        dto.add(linkTo(methodOn(PersonController.class)
                .findById(dto.getId())) // passando o id como parâmetro
                .withSelfRel() // redirecionando a URL onde acontecerá a ocorrência
                .withType("GET")); // tipo de método HTTP

        dto.add(linkTo(methodOn(PersonController.class)
                .findAll()) // sem parâmetros
                .withRel("findAll") // passamos o relacionamento dentro dos parenteses, nesse caso é o findAll
                .withType("GET")); // tipo de método HTTP

        dto.add(linkTo(methodOn(PersonController.class)
                .create(dto)) // passando o PersonDTO como parâmetro
                .withRel("create") // passamos o relacionamento dentro dos parenteses, nesse caso é o create
                .withType("POST")); // tipo de método HTTP

        dto.add(linkTo(methodOn(PersonController.class)
                .update(dto)) // passando o PersonDTO como parâmetro
                .withRel("update") // passamos o relacionamento dentro dos parenteses, nesse caso é o update
                .withType("PUT")); // tipo de método HTTP


        dto.add(linkTo(methodOn(PersonController.class)
                .delete(dto.getId())) // passando o id como parâmetro
                .withRel("delete") // passamos o relacionamento dentro dos parenteses, nesse caso é o delete
                .withType("DELETE")); // tipo de método HTTP
    }
}
