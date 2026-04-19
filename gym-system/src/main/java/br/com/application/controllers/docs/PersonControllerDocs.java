package br.com.application.controllers.docs;

import br.com.application.data.dto.PersonDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface PersonControllerDocs {

    // documentando com o Swagger
    @Operation(summary = "Finds a Person", // aqui é o título do Endpoint
            description = "Finds a specific person by your ID", // adicionando a descrição do Endpoint
            tags = {"People"}, // tags do Endpoint
            responses = {
                    @ApiResponse(description = "Success",
                            responseCode = "200", // exibindo o tipo de Status Code (200 = Sucess)
                            content =
                            @Content(schema = @Schema(implementation = PersonDTO.class)) // com base no PersonDTO o JSON já é montado
                    ),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content), // exibindo o tipo de Status Code (204 = No Content)
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content), // exibindo o tipo de Status Code (400 = Bad Request)
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content), // exibindo o tipo de Status Code (401 = Unauthorized)
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content), // exibindo o tipo de Status Code (404 = Not Found)
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content), // exibindo o tipo de Status Code (500 = Internal Server Error)
            }
    )
    PersonDTO findById(@PathVariable("id") Long id);

    // documentando com o Swagger
    @Operation(summary = "Find All People", // aqui é o título do Endpoint
            description = "Finds All People", // adicionando a descrição do Endpoint
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
    List<PersonDTO> findAll();

    // documentando com o Swagger
    @Operation(summary = "Create a new Person", // aqui é o título do Endpoint
            description = "Adds a new person by passing in a JSON, XML or YML representation of the person.", // adicionando a descrição do Endpoint
            tags = {"People"}, // tags do Endpoint
            responses = {
                    @ApiResponse(description = "Success",
                            responseCode = "200", // exibindo o tipo de Status Code (200 = Sucess)
                            content =
                            @Content(schema = @Schema(implementation = PersonDTO.class)) // com base no PersonDTO o JSON já é montado
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content), // exibindo o tipo de Status Code (400 = Bad Request)
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content), // exibindo o tipo de Status Code (401 = Unauthorized)
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content), // exibindo o tipo de Status Code (500 = Internal Server Error)
            }
    )
    PersonDTO create(@RequestBody PersonDTO personDTO);

    // documentando com o Swagger
    @Operation(summary = "Updates a Person's information", // aqui é o título do Endpoint
            description = "Updating a Person's information by passing in a JSON, XML or YML representation of the person.", // adicionando a descrição do Endpoint
            tags = {"People"}, // tags do Endpoint
            responses = {
                    @ApiResponse(description = "Success",
                            responseCode = "200", // exibindo o tipo de Status Code (200 = Sucess)
                            content =
                            @Content(schema = @Schema(implementation = PersonDTO.class)) // com base no PersonDTO o JSON já é montado
                    ),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content), // exibindo o tipo de Status Code (204 = No Content)
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content), // exibindo o tipo de Status Code (400 = Bad Request)
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content), // exibindo o tipo de Status Code (401 = Unauthorized)
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content), // exibindo o tipo de Status Code (404 = Not Found)
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content), // exibindo o tipo de Status Code (500 = Internal Server Error)
            }
    )
    PersonDTO update(@RequestBody PersonDTO personDTO);

    // documentando com o Swagger
    @Operation(summary = "Disable a Person", // aqui é o título do Endpoint
            description = "Disable a specific person by your ID", // adicionando a descrição do Endpoint
            tags = {"People"}, // tags do Endpoint
            responses = {
                    @ApiResponse(description = "Success",
                            responseCode = "200", // exibindo o tipo de Status Code (200 = Sucess)
                            content =
                            @Content(schema = @Schema(implementation = PersonDTO.class)) // com base no PersonDTO o JSON já é montado
                    ),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content), // exibindo o tipo de Status Code (204 = No Content)
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content), // exibindo o tipo de Status Code (400 = Bad Request)
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content), // exibindo o tipo de Status Code (401 = Unauthorized)
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content), // exibindo o tipo de Status Code (404 = Not Found)
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content), // exibindo o tipo de Status Code (500 = Internal Server Error)
            }
    )
    PersonDTO disablePerson(@PathVariable("id") Long id);

    // documentando com o Swagger
    @Operation(summary = "Deletes a Person", // aqui é o título do Endpoint
            description = "Deletes a specific person by their ID.", // adicionando a descrição do Endpoint
            tags = {"People"}, // tags do Endpoint
            responses = {
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content), // exibindo o tipo de Status Code (204 = No Content)
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content), // exibindo o tipo de Status Code (400 = Bad Request)
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content), // exibindo o tipo de Status Code (401 = Unauthorized)
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content), // exibindo o tipo de Status Code (404 = Not Found)
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content), // exibindo o tipo de Status Code (500 = Internal Server Error)
            }
    )
    ResponseEntity<?> delete(@PathVariable("id") Long id);
}
