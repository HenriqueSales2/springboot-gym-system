package br.com.application.controllers.docs;

import br.com.application.data.dto.PersonalDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface PersonalControllerDocs {

    @Operation(summary = "Finds a Personal",
            description = "Finds a specific Personal by your ID",
            tags = {"Teachers"},
            responses = {
                    @ApiResponse(description = "Sucess",
                            responseCode = "200",
                            content =
                            @Content(schema = @Schema(implementation = PersonalDTO.class))
                    ),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    PersonalDTO findPersonalById(@PathVariable("id") Long id);


    @Operation(summary = "Find All Teachers",
            description = "Finds All Teachers",
            tags = {"Teachers"},
            responses = {
                    @ApiResponse(description = "Sucess",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            array = @ArraySchema(schema = @Schema(implementation = PersonalDTO.class))
                                    )
                            }),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    ResponseEntity<PagedModel<EntityModel<PersonalDTO>>> findAllTeachers(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    );


    @Operation(summary = "Create a new Personal",
            description = "Adds a new Personal by passing in a JSON, XML or YML representation of the Personal.",
            tags = {"Teachers"},
            responses = {
                    @ApiResponse(description = "Sucess",
                            responseCode = "200",
                            content =
                            @Content(schema = @Schema(implementation = PersonalDTO.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    PersonalDTO create(@RequestBody PersonalDTO personalDTO);


    @Operation(summary = "Updates a Teacher's information",
            description = "Updating a Teacher's information by passing in a JSON, XML or YML representation of the Personal.",
            tags = {"Teachers"},
            responses = {
                    @ApiResponse(description = "Sucess",
                            responseCode = "200",
                            content =
                            @Content(schema = @Schema(implementation = PersonalDTO.class))
                    ),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    PersonalDTO update(@RequestBody PersonalDTO personalDTO);


    @Operation(summary = "Deletes a Personal",
            description = "Deletes a specific Personal by their ID.",
            tags = {"Teachers"},
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
