package br.com.application.controllers.person;

import br.com.application.controllers.docs.PersonControllerDocs;
import br.com.application.data.dto.PersonDTO;
import br.com.application.service.PersonService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/* especificar o dominio do site que vai ser acessado pelo cliente,
 como estou rodando localhost vou atribuí-lo ao Cross Origin.
 O correto é habilitar o CORS de Forma Global através da Classe de
 Configuration, em último caso Annotations específicas para cada Endpoint
*/
// @CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/person/v1")
@Tag(name = "People", description = "Endpoints for Managing People")
public class PersonController implements PersonControllerDocs {

    @Autowired
    private PersonService service; // com a dependência Service
    // private PersonService service =  new PersonService(); sem a dependência Service


    @GetMapping(
            value = "/{id}",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE
            }
    )
    @Override //  criei uma interface para a documentação dos Endpoints no Swagger. Diante disso, surge uma annotation obrigatória
    public PersonDTO findById(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    // é o mesmo que adicionar essa String "application/json"
    @GetMapping(
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE
            }
    )
    @Override
    public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findAll(
        @RequestParam (value = "page", defaultValue = "0") Integer page,
        @RequestParam (value = "size", defaultValue = "12") Integer size,
        @RequestParam (value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));
        return ResponseEntity.ok(service.findAll(pageable));

    }

    @GetMapping(
            value = "/findPeopleByName/{firstName}",
            produces = { // produz JSON, ou seja, me retorna um JSON
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE
            }
    )
    @Override
    public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findPeopleByName(
            @PathVariable("firstName") String firstName,
            @RequestParam(value = "page", defaultValue = "0") Integer page, // aqui são o número de páginas, por padrão ele vai retornar a primeira página (que nesse caso é o valor "0")
            @RequestParam(value = "size", defaultValue = "12") Integer size, // tamanho da página, caso eu não especificar nada, ele retorna a quantidade de itens setada por padrão (que nesse caso é "12")
            @RequestParam(value = "direction", defaultValue = "asc") String direction // aqui é a direção da página, se ela é em ordem ascendente (crescente) ou descendente (decrescente), por padrão deixei ascendente (que nesse caso é "asc")
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC; // SE for igual à "desc", independente se for maiúsculo ou minúsculo na hora de passar o parâmetro ele vai ordenar a lista em ordem decrescente, caso contrário vai ser em ordem crescente
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName")); // lembrando que na hora da escolha para ordenar é preciso colocar o nome igual o da váriavel, pois é Case Sensitive
        return ResponseEntity.ok(service.findPeopleByName(firstName, pageable));
    }

    // exemplo de implementação do CORS à um Endpoint específico
    //@CrossOrigin(origins = {"http://localhost:8080", "https://example.com.br"})
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
    public PersonDTO create(@RequestBody PersonDTO personDTO) {
        return service.create(personDTO);
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
