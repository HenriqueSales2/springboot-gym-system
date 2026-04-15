package br.com.application.controllers.docs;

import br.com.application.data.dto.PersonalDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface PersonalControllerDocs {
    // documentando com o Swagger
    @Operation(summary = "Finds a Personal", // aqui é o título do Endpoint
            description = "Finds a specific Personal by your ID", // adicionando a descrição do Endpoint
            tags = {"Teachers"}, // tags do Endpoint
            responses = {
                    @ApiResponse(description = "Sucess",
                            responseCode = "200", // exibindo o tipo de Status Code (200 = Sucess)
                            content =
                            @Content(schema = @Schema(implementation = PersonalDTO.class)) // com base no PersonalDTO o JSON já é montado
                    ),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content), // exibindo o tipo de Status Code (204 = No Content)
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content), // exibindo o tipo de Status Code (400 = Bad Request)
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content), // exibindo o tipo de Status Code (401 = Unauthorized)
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content), // exibindo o tipo de Status Code (404 = Not Found)
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content), // exibindo o tipo de Status Code (500 = Internal Server Error)
            }
    )
    PersonalDTO findPersonalById(@PathVariable("id") Long id);

    // documentando com o Swagger
    @Operation(summary = "Find All Teachers", // aqui é o título do Endpoint
            description = "Finds All Teachers", // adicionando a descrição do Endpoint
            tags = {"Teachers"}, // tags do Endpoint
            responses = {
                    @ApiResponse(description = "Sucess",
                            responseCode = "200", // exibindo o tipo de Status Code (200 = Sucess)
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            array = @ArraySchema(schema = @Schema(implementation = PersonalDTO.class)) // com base no PersonalDTO o JSON já é montado
                                    )
                            }),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content), // exibindo o tipo de Status Code (204 = No Content)
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content), // exibindo o tipo de Status Code (400 = Bad Request)
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content), // exibindo o tipo de Status Code (401 = Unauthorized)
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content), // exibindo o tipo de Status Code (404 = Not Found)
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content), // exibindo o tipo de Status Code (500 = Internal Server Error)
            }
    )
    List<PersonalDTO> findAllTeachers();

    // documentando com o Swagger
    @Operation(summary = "Create a new Personal", // aqui é o título do Endpoint
            description = "Adds a new Personal by passing in a JSON, XML or YML representation of the Personal.", // adicionando a descrição do Endpoint
            tags = {"Teachers"}, // tags do Endpoint
            responses = {
                    @ApiResponse(description = "Sucess",
                            responseCode = "200", // exibindo o tipo de Status Code (200 = Sucess)
                            content =
                            @Content(schema = @Schema(implementation = PersonalDTO.class)) // com base no PersonalDTO o JSON já é montado
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content), // exibindo o tipo de Status Code (400 = Bad Request)
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content), // exibindo o tipo de Status Code (401 = Unauthorized)
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content), // exibindo o tipo de Status Code (500 = Internal Server Error)
            }
    )
    PersonalDTO create(@RequestBody PersonalDTO personalDTO);

    // documentando com o Swagger
    @Operation(summary = "Updates a Teacher's information", // aqui é o título do Endpoint
            description = "Updating a Teacher's information by passing in a JSON, XML or YML representation of the Personal.", // adicionando a descrição do Endpoint
            tags = {"Teachers"}, // tags do Endpoint
            responses = {
                    @ApiResponse(description = "Sucess",
                            responseCode = "200", // exibindo o tipo de Status Code (200 = Sucess)
                            content =
                            @Content(schema = @Schema(implementation = PersonalDTO.class)) // com base no PersonalDTO o JSON já é montado
                    ),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content), // exibindo o tipo de Status Code (204 = No Content)
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content), // exibindo o tipo de Status Code (400 = Bad Request)
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content), // exibindo o tipo de Status Code (401 = Unauthorized)
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content), // exibindo o tipo de Status Code (404 = Not Found)
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content), // exibindo o tipo de Status Code (500 = Internal Server Error)
            }
    )
    PersonalDTO update(@RequestBody PersonalDTO personalDTO);

    // documentando com o Swagger
    @Operation(summary = "Deletes a Personal", // aqui é o título do Endpoint
            description = "Deletes a specific Personal by their ID.", // adicionando a descrição do Endpoint
            tags = {"Teachers"}, // tags do Endpoint
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
