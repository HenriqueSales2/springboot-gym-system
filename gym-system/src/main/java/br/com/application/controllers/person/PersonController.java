package br.com.application.controllers.person;

import br.com.application.config.SecurityConfig;
import br.com.application.controllers.docs.PersonControllerDocs;
import br.com.application.data.dto.PersonDTO;
import br.com.application.file.exporter.MediaTypes;
import br.com.application.service.person.PersonService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/*
especificar o dominio do site que vai ser acessado pelo cliente,
 como estou rodando localhost vou atribuí-lo ao Cross Origin.
 O correto é habilitar o CORS de Forma Global através da Classe de
 Configuration, em último caso Annotations específicas para cada Endpoint
*/
// @CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/person/v1")
@Tag(name = "People", description = "Endpoints for Managing People")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class PersonController implements PersonControllerDocs {

    @Autowired
    private PersonService service;

    @GetMapping(
            produces = { // produz JSON, ou seja, me retorna um JSON
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE
            }
    )
    @Override //  criei uma interface para a documentação dos Endpoints no Swagger. Diante disso, surge uma annotation obrigatória
    public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findAll(
            @RequestParam (value = "page", defaultValue = "0") Integer page, // aqui são o número de páginas, por padrão ele vai retornar a primeira página (que nesse caso é o valor "0")
            @RequestParam (value = "size", defaultValue = "12") Integer size, // tamanho da página, caso eu não especificar nada, ele retorna a quantidade de itens setada por padrão (que nesse caso é "12")
            @RequestParam (value = "direction", defaultValue = "asc") String direction // aqui é a direção da página, se ela é em ordem ascendente (crescente) ou descendente (decrescente), por padrão deixei ascendente (que nesse caso é "asc")
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC; // SE for igual à "desc", independente se for maiúsculo ou minúsculo na hora de passar o parâmetro ele vai ordenar a lista em ordem decrescente, caso contrário vai ser em ordem crescente
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName")); // lembrando que na hora da escolha para ordenar é preciso colocar o nome igual o da váriavel, pois é Case Sensitive
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping(
            value = "/findPeopleByName/{firstName}",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE
            }
    )
    @Override
    public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findPeopleByName(
            @PathVariable("firstName") String firstName,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));
        return ResponseEntity.ok(service.findPeopleByName(firstName, pageable));
    }

    @GetMapping(
            value = "/{id}",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE
            }
    )
    @Override
    public PersonDTO findById(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @GetMapping(
            value = "/exportPerson/{id}",
            produces = MediaTypes.APPLICATION_PDF_VALUE
    )
    @Override
    public ResponseEntity<Resource> exportPerson(@PathVariable("id") Long id, HttpServletRequest request) {
        String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);

        Resource file = service.exportPerson(id, acceptHeader);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(acceptHeader))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=person.pdf"
                )
                .body(file);
    }

    @GetMapping(
            value = "/exportPage",
            produces = {
                    MediaTypes.APPLICATION_XLSX_VALUE,
                    MediaTypes.APPLICATION_CSV_VALUE,
                    MediaTypes.APPLICATION_PDF_VALUE
            }
    )
    @Override
    public ResponseEntity<Resource> exportPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            HttpServletRequest request
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));

        String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);

        Resource file = service.exportPage(pageable, acceptHeader);

        Map<String, String> extensionMap = Map.of(
                MediaTypes.APPLICATION_XLSX_VALUE, ".xlsx",
                MediaTypes.APPLICATION_CSV_VALUE, ".csv",
                MediaTypes.APPLICATION_PDF_VALUE, ".pdf"
        );

        var fileExtension = extensionMap.getOrDefault(acceptHeader, "");

        var contentType = acceptHeader != null ? acceptHeader : "application/octet-stream";

        var fileName = "people" + fileExtension;

        return ResponseEntity.ok() // por fim, retorna uma Reponse Entity
                .contentType(MediaType.parseMediaType(contentType)) // contendo o tipo de contéudo (contenty type), convertido para parseMediaType
                .header(
                        HttpHeaders.CONTENT_DISPOSITION, //dizendo que na Header da Response será mandado um anexo
                        "attachment; filename=\"" + fileName + "\"" // e esse anexo está definido aqui
                )
                .body(file); // no corpo da Response, passamos o arquivo
    }

    @PostMapping(
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE
            },
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE
            }
    )
    @Override
    public ResponseEntity<PersonDTO> create(@RequestBody PersonDTO personDTO) {
        var user = service.create(personDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping( value = "/massCreation",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE
            }
    )
    @Override
    public List<PersonDTO> massCreation(@RequestParam("file") MultipartFile file) {
        return service.massCreation(file);
    }

    @PutMapping(
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE
            },
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE
            }
    )
    @Override
    public PersonDTO update(@RequestBody PersonDTO personDTO) {
        return service.update(personDTO);
    }

    @PatchMapping(
            value = "/{id}",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE
            }
    )
    @Override
    public PersonDTO disablePerson(@PathVariable("id") Long id) {
        return service.disablePerson(id);
    }

    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build(); // devolver a requisição sem corpo
    }
}