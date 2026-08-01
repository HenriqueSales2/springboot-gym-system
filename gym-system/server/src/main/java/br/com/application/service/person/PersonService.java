package br.com.application.service.person;

import br.com.application.controllers.person.PersonController;
import br.com.application.data.dto.PersonDTO;
import br.com.application.exception.*;

import static br.com.application.mapper.ObjectMapper.parseObject;

import br.com.application.file.exporter.contract.PersonExporter;
import br.com.application.file.exporter.factory.FileExporterFactory;
import br.com.application.file.importer.contract.FileImporter;
import br.com.application.file.importer.factory.FileImporterFactory;
import br.com.application.model.Person;
import br.com.application.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private Logger logger = LoggerFactory.getLogger(PersonController.class);

    @Autowired
    PersonRepository repository;

    @Autowired
    private FileImporterFactory importer;

    @Autowired
    private FileExporterFactory exporter;

    @Autowired
    private PagedResourcesAssembler<PersonDTO> assembler;

    public PagedModel<EntityModel<PersonDTO>> findAll(Pageable pageable) {
        logger.info("Finding all People!");

        var people = repository.findAll(pageable);

        return buildPagedModel(pageable, people);
    }

    public PagedModel<EntityModel<PersonDTO>> findPeopleByName(String firstName, Pageable pageable) {
        logger.info("Finding People by Name!");

        var people = repository.findPeopleByName(firstName, pageable);

        return buildPagedModel(pageable, people);
    }

    public PersonDTO findById(Long id) {
        logger.info("Finding one Person!");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No record found for this ID!"));
        var dto = parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public Resource exportPerson(Long id, String acceptHeader) {
        logger.info("Exporting data of one Person!");

        var person = repository.findById(id)
                .map(entity -> parseObject(entity, PersonDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("No record found for this ID!"));

        try {
            PersonExporter fileExporter = this.exporter.getExporter(acceptHeader);
            return fileExporter.exportPerson(person);
        }
        catch (Exception e) {
            throw new FileNotExportingException("Error during file export!", e);
        }
    }

    public Resource exportPage(Pageable pageable, String acceptHeader) {
        logger.info("Exporting a People page!");

        var people = repository.findAll(pageable)
                .map(person -> parseObject(person, PersonDTO.class))
                .getContent();

        try {
            PersonExporter personExporter = this.exporter.getExporter(acceptHeader);
            return personExporter.exportPeople(people);
        } catch (Exception e) {
            throw new FileNotExportingException("Error during file export!", e);
        }
    }

    public PersonDTO create(PersonDTO personDTO) {
        if (personDTO == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one Person!");

        var entity = parseObject(personDTO, Person.class);
        var dto = parseObject(repository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public List<PersonDTO> massCreation(MultipartFile file) {
        logger.info("Importing People from file!");

        if (file.isEmpty()) throw new BadRequestException("Please set a Valid File!");

        try (InputStream inputStream = file.getInputStream()){
            String fileName = Optional.ofNullable(file.getOriginalFilename())
                    .orElseThrow(() -> new BadRequestException("File name cannot be null!"));

            FileImporter fileImporter = this.importer.getImporter(fileName);

            List<Person> entities = fileImporter.importFile(inputStream)
                    .stream()
                    .map(personDTO -> repository.save(parseObject(personDTO, Person.class)))
                    .toList();

            return entities.stream()
                    .map(entity -> {

                        var dto = parseObject(entity, PersonDTO.class);
                        addHateoasLinks(dto);
                        return dto;

                    })
                    .toList();
        } catch (Exception e) {
            throw new FileStorageException("Error processing the file!");
        }
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
        person.setEnabled(personDTO.getEnabled());
        person.setProfileUrl(personDTO.getProfileUrl());
        person.setPhotoUrl(personDTO.getPhotoUrl());

        var entity = repository.save(person);
        var dto = parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    @Transactional
    public PersonDTO disablePerson(Long id) {
        logger.info("Disable a Person!");

        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No record found for this ID!"));
        repository.disablePerson(id);
        var entity = repository.findById(id).get();
        var dto = parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public void delete(Long id) {
        logger.info("Delete one Person!");

        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No record found for this ID!"));
        repository.delete(entity);
    }

    private PagedModel<EntityModel<PersonDTO>> buildPagedModel(Pageable pageable, Page<Person> people) {
        var peopleWithLinks = people.map(person -> {
            var dto = parseObject(person, PersonDTO.class);
            addHateoasLinks(dto);
            return dto;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo
                        (
                                WebMvcLinkBuilder.methodOn(PersonController.class)
                                        .findAll(pageable.getPageNumber(), pageable.getPageSize(), String.valueOf(pageable.getSort()))
                        )
                .withSelfRel();
        return assembler.toModel(peopleWithLinks, findAllLink);
    }

    private static void addHateoasLinks(PersonDTO dto) {
        dto.add(linkTo(methodOn(PersonController.class)
                .findAll(1, 12, "asc"))
                .withRel("findAll")
                .withType("GET"));

        dto.add(linkTo(methodOn(PersonController.class)
                .findPeopleByName(dto.getFirstName(), 1, 12, "asc"))
                .withRel("findPeopleByName")
                .withType("GET"));

        dto.add(linkTo(methodOn(PersonController.class)
                .findById(dto.getId()))
                .withSelfRel()
                .withType("GET"));

        dto.add(linkTo(methodOn(PersonController.class)
                .exportPerson(dto.getId(), null))
                .withRel("exportPerson")
                .withType("GET")
                .withTitle("Export Person"));

        dto.add(linkTo(methodOn(PersonController.class)
                .exportPage(
                        1, 12, "asc", null)
        )
                .withRel("exportPage")
                .withType("GET")
                .withTitle("Export People"));

        dto.add(linkTo(methodOn(PersonController.class)
                .create(dto))
                .withRel("create")
                .withType("POST"));

        dto.add(linkTo(methodOn(PersonController.class))
                .slash("massCreation")
                .withRel("massCreation")
                .withType("POST"));

        dto.add(linkTo(methodOn(PersonController.class)
                .update(dto))
                .withRel("update")
                .withType("PUT"));

        dto.add(linkTo(methodOn(PersonController.class)
                .disablePerson(dto.getId()))
                .withRel("disablePerson")
                .withType("PATCH"));

        dto.add(linkTo(methodOn(PersonController.class)
                .delete(dto.getId()))
                .withRel("delete")
                .withType("DELETE"));
    }
}