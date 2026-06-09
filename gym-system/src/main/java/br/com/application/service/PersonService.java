package br.com.application.service;

import br.com.application.controllers.person.PersonController;
import br.com.application.controllers.person.TestLogController;
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

@Service // serve para injetar dependências sem ficar dando "new Objeto" por exemplo
public class PersonService {

    private Logger logger = LoggerFactory.getLogger(TestLogController.class.getName()); // logar informações importantes do projeto

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

        var peopleWithLinks = people.map(person -> {
                    var dto = parseObject(person, PersonDTO.class); // convertendo em DTO, criando, e salvando a entidade
                    addHateoasLinks(dto); // adicionando os links Hateoas
                    return dto; // depois retornamos a entidade convertida junto com os links Hateoas
                }
        );

        Link findAllLink = WebMvcLinkBuilder.linkTo
                (
                        WebMvcLinkBuilder.methodOn(PersonController.class)
                                .findAll(pageable.getPageNumber(), pageable.getPageSize(), String.valueOf(pageable.getSort()))
                ).withSelfRel();
        return assembler.toModel(peopleWithLinks, findAllLink);
    }

    public PagedModel<EntityModel<PersonDTO>> findPeopleByName(String firstName, Pageable pageable) {
        logger.info("Finding People by Name!");

        var people = repository.findPeopleByName(firstName, pageable);

        var peopleWithLinks = people.map(person -> {
            var dto = parseObject(person, PersonDTO.class); // convertendo em DTO, criando, e salvando a entidade
            addHateoasLinks(dto); // adicionando os links Hateoas
            return dto; // depois retornamos a entidade convertida junto com os links Hateoas
        });

        Link findAllLink  = WebMvcLinkBuilder.linkTo
                        (
                                WebMvcLinkBuilder.methodOn(PersonController.class)
                                        .findAll(pageable.getPageNumber(), pageable.getPageSize(), String.valueOf(pageable.getSort()))
                        )
                .withSelfRel();

        return assembler.toModel(peopleWithLinks, findAllLink);
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
                .map(person -> parseObject(person, PersonDTO.class)) // mapeia item por item, convertendo para PersonDTO
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

        var entity = parseObject(personDTO, Person.class); // fazendo a conversão de PersonDTO para Person
        var dto = parseObject(repository.save(entity), PersonDTO.class); // convertendo em DTO, criando, e salvando a entidade
        addHateoasLinks(dto);
        return dto;
    }

    public List<PersonDTO> massCreation(MultipartFile file) {
        logger.info("Importing People from file!");

        if (file.isEmpty()) throw new BadRequestException("Please set a Valid File!"); // verifica se esse MultipartFile está preenchido, caso contrário lança uma exceção

        try (InputStream inputStream = file.getInputStream()){ // cria o inputStream
            String fileName = Optional.ofNullable(file.getOriginalFilename()) // obtém o nome do inputStream
                    .orElseThrow(() -> new BadRequestException("File name cannot be null!"));

            FileImporter fileImporter = this.importer.getImporter(fileName); // o nome é necessário para saber a instância do importer precisará usar na FileImporterFactory

            List<Person> entities = fileImporter.importFile(inputStream) // chama o "fileImporter" (importador) e importa usando o "inputStream"
                    .stream()
                    .map(personDTO -> repository.save(parseObject(personDTO, Person.class))) // converte a lista de DTOs para entidades e salva no banco de dados
                    .toList(); // adiciona a uma lista de entidades

            return entities.stream()
                    .map(entity -> {

                        var dto = parseObject(entity, PersonDTO.class); // converte a lista de entidades para DTOs
                        addHateoasLinks(dto); // para assim adicionar os links HATEOAS
                        return dto; // enfim retornar a lista

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

        var entity = repository.save(person);
        var dto = parseObject(entity, PersonDTO.class); // convertendo em DTO, atualizando, e salvando a entidade
        addHateoasLinks(dto);
        return dto;
    }

    @Transactional // adicionando essa anotação chamada "Transactional", pois não é um método oficial do Spring JPA
    public PersonDTO disablePerson(Long id) {
        logger.info("Disable a Person!");

        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No record found for this ID!"));
        repository.disablePerson(id);
        var entity = repository.findById(id).get(); // pegando a entidade direto, pois se não existir id, cai em uma exceção
        var dto = parseObject(entity, PersonDTO.class); // convertendo em DTO, atualizando, e salvando a entidade
        addHateoasLinks(dto); // adicionando os links Hateoas à entidade convertida
        return dto;
    }

    public void delete(Long id) {
        logger.info("Delete one Person!");

        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No record found for this ID!"));
        repository.delete(entity);
    }

    // criando um método para a construção da página para evitar a reutilização de código e deixar a camada service da aplicação mais enxuta
    private PagedModel<EntityModel<PersonDTO>> buildPagedModel(Pageable pageable, Page<Person> people) {
        var peopleWithLinks = people.map(person -> {
            var dto = parseObject(person, PersonDTO.class); // convertendo em DTO, criando, e salvando a entidade
            addHateoasLinks(dto); // adicionando os links Hateoas
            return dto; // depois retornamos a entidade convertida junto com os links Hateoas
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
                .withRel("findAll") // redirecionando a URL onde acontecerá a ocorrência
                .withType("GET")); // tipo de método HTTP

        dto.add(linkTo(methodOn(PersonController.class)
                .findPeopleByName(dto.getFirstName(), 1, 12, "asc"))
                .withRel("findPeopleByName")
                .withType("GET"));

        dto.add(linkTo(methodOn(PersonController.class)
                .findById(dto.getId())) // passando o id como parâmetro
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
