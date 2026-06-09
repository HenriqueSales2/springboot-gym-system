package br.com.application.controllers.docs;

import br.com.application.data.dto.PersonDTO;
import br.com.application.file.exporter.MediaTypes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PersonControllerDocs {

    // documentando com o Swagger
    @Operation(summary = "Find All People", // aqui é o título do Endpoint
            description = "Find's All People", // adicionando a descrição do Endpoint
            tags = {"People"}, // tags do Endpoint
            responses = {
                    @ApiResponse(description = "Success",
                            responseCode = "200", // exibindo o tipo de Status Code (200 = Sucess)
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class)) // com base no PersonDTO o JSON já é montado
                                    )
                            }),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content), // exibindo o tipo de Status Code (204 = No Content)
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content), // exibindo o tipo de Status Code (400 = Bad Request)
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content), // exibindo o tipo de Status Code (401 = Unauthorized)
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content), // exibindo o tipo de Status Code (404 = Not Found)
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content), // exibindo o tipo de Status Code (500 = Internal Server Error)
            }
    )
    ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page, // aqui são o número de páginas, por padrão ele vai retornar a primeira página (que nesse caso é o valor "0")
            @RequestParam(value = "size", defaultValue = "12") Integer size, // tamanho da página, caso eu não especificar nada, ele retorna a quantidade de itens setada por padrão (que nesse caso é "12")
            @RequestParam(value = "direction", defaultValue = "asc") String direction // aqui é a direção da página, se ela é em ordem ascendente (crescente) ou descendente (decrescente), por padrão deixei ascendente (que nesse caso é "asc")
    );

    @Operation(summary = "Find People by FirstName",
            description = "Find's People by their First Names",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class))
                                    )
                            }),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findPeopleByName(
            @PathVariable("firstName") String firstName,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    );

    @Operation(summary = "Finds a Person",
            description = "Find's a specific person by your ID",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "Success",
                            responseCode = "200",
                            content =
                            @Content(schema = @Schema(implementation = PersonDTO.class))
                    ),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    PersonDTO findById(@PathVariable("id") Long id);

    @Operation(summary = "Export Person data as PDF",
            description = "Export a specific Person data as PDF by your ID",
            tags = {"People"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(mediaType = MediaTypes.APPLICATION_PDF_VALUE)
                    ),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    ResponseEntity<Resource> exportPerson(
            @PathVariable("id") Long id,
            HttpServletRequest request
    );

    @Operation(summary = "Export People",
            description = "Export a Page of People in XLSX, CSV and PDF format",
            tags = {"People"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(mediaType = MediaTypes.APPLICATION_XLSX_VALUE),
                                    @Content(mediaType = MediaTypes.APPLICATION_CSV_VALUE),
                                    @Content(mediaType = MediaTypes.APPLICATION_PDF_VALUE)
                            }),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    ResponseEntity<Resource> exportPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            HttpServletRequest request
    );

    @Operation(summary = "Create a new Person",
            description = "Adds a new person by passing in a JSON, XML or YML representation of the person.",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "Success",
                            responseCode = "200",
                            content =
                            @Content(schema = @Schema(implementation = PersonDTO.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    PersonDTO create(@RequestBody PersonDTO personDTO);

    @Operation(summary = "Massive People Creation",
            description = "Massive People Creation with upload of XLSX or CSV",
            tags = {"People"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(schema = @Schema(implementation = PersonDTO.class))
                            }),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    List<PersonDTO> massCreation(MultipartFile file);

    @Operation(summary = "Updates a Person's information",
            description = "Update a Person's information by passing in a JSON, XML or YML representation of the person.",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "Success",
                            responseCode = "200",
                            content =
                            @Content(schema = @Schema(implementation = PersonDTO.class))
                    ),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    PersonDTO update(@RequestBody PersonDTO personDTO);

    @Operation(summary = "Disable a Person",
            description = "Disable a specific person by your ID",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "Success",
                            responseCode = "200",
                            content =
                            @Content(schema = @Schema(implementation = PersonDTO.class))
                    ),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    PersonDTO disablePerson(@PathVariable("id") Long id);

    @Operation(summary = "Delete a Person",
            description = "Delete's a specific person by their ID.",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    ResponseEntity<?> delete(@PathVariable("id") Long id);
}