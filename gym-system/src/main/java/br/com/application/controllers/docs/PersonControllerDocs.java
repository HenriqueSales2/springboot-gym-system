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

    @Operation(summary = "Find All People",
            description = "Retrieve a paginated list of all registered people",
            tags = {"People"},
            responses = {
                    @ApiResponse(
                            description = "People retrieved successfully",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class))
                                    )
                            }),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    );

    @Operation(summary = "Find People by FirstName",
            description = "Retrieve a paginated list of people filtered by first name",
            tags = {"People"},
            responses = {
                    @ApiResponse(
                            description = "People retrieved successfully",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class))
                                    )
                            }),
                    @ApiResponse(description = "Invalid request parameters", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findPeopleByName(
            @PathVariable("firstName") String firstName,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    );

    @Operation(summary = "Find Person by ID",
            description = "Retrieve a specific person using their identifier",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "Person retrieved successfully", responseCode = "200", content = @Content(schema = @Schema(implementation = PersonDTO.class))),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Person not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    PersonDTO findById(@PathVariable("id") Long id);

    @Operation(summary = "Export Person as PDF",
            description = "Export a specific person's data as a PDF file",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "Person exported successfully", responseCode = "200", content = @Content(mediaType = MediaTypes.APPLICATION_PDF_VALUE)),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Person not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Error exporting file", responseCode = "500", content = @Content),
            }
    )
    ResponseEntity<Resource> exportPerson(
            @PathVariable("id") Long id,
            HttpServletRequest request
    );

    @Operation(summary = "Export People",
            description = "Export a page of people in XLSX, CSV or PDF format.",
            tags = {"People"},
            responses = {
                    @ApiResponse(
                            description = "People exported successfully",
                            responseCode = "200",
                            content = {
                                    @Content(mediaType = MediaTypes.APPLICATION_XLSX_VALUE),
                                    @Content(mediaType = MediaTypes.APPLICATION_CSV_VALUE),
                                    @Content(mediaType = MediaTypes.APPLICATION_PDF_VALUE)
                            }),
                    @ApiResponse(description = "Invalid request parameters", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Error exporting file", responseCode = "500", content = @Content),
            }
    )
    ResponseEntity<Resource> exportPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            HttpServletRequest request
    );

    @Operation(summary = "Create Person",
            description = "Create a new person using JSON, XML or YAML input",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "Person created successfully", responseCode = "201", content = @Content(schema = @Schema(implementation = PersonDTO.class))),
                    @ApiResponse(description = "Invalid request body", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    ResponseEntity<PersonDTO> create(@RequestBody PersonDTO personDTO);

    @Operation(summary = "Create Multiple People",
            description = "Create multiple people by importing an XLSX or CSV file",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "People created successfully", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class)))),
                    @ApiResponse(description = "Invalid file", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Error processing file", responseCode = "500", content = @Content),
            }
    )
    List<PersonDTO> massCreation(MultipartFile file);

    @Operation(summary = "Update Person",
            description = "Update an existing person's information by passing in a JSON, XML or YML representation of the person",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "Person updated successfully", responseCode = "200", content = @Content(schema = @Schema(implementation = PersonDTO.class))),
                    @ApiResponse(description = "Invalid request body", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Person not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    PersonDTO update(@RequestBody PersonDTO personDTO);

    @Operation(summary = "Disable a Person",
            description = "Disable a specific person using their identifier",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "Person disabled successfully", responseCode = "200", content = @Content(schema = @Schema(implementation = PersonDTO.class))),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Person not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    PersonDTO disablePerson(@PathVariable("id") Long id);

    @Operation(summary = "Delete Person",
            description = "Delete a specific person using their identifier",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "Person deleted successfully", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Person not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    ResponseEntity<?> delete(@PathVariable("id") Long id);
}