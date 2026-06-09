package br.com.application.controllers.docs;

import br.com.application.data.dto.WorkoutDTO;
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

public interface WorkoutControllerDocs {

    @Operation(summary = "Finds a Training",
            description = "Find's a specific Training by your ID",
            tags = {"Workouts"},
            responses = {
                    @ApiResponse(description = "Sucess",
                            responseCode = "200",
                            content =
                            @Content(schema = @Schema(implementation = WorkoutDTO.class))
                    ),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    WorkoutDTO findWorkoutById(@PathVariable("id") Long id);

    @Operation(summary = "Find All Workouts",
            description = "Find's All Workouts",
            tags = {"Workouts"},
            responses = {
                    @ApiResponse(description = "Sucess",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            array = @ArraySchema(schema = @Schema(implementation = WorkoutDTO.class))
                                    )
                            }),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    ResponseEntity<PagedModel<EntityModel<WorkoutDTO>>> findAllWorkouts(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    );

    @Operation(summary = "Create a new Workout",
            description = "Create a new Workout by passing in a JSON, XML or YML representation of the Workouts.",
            tags = {"Workouts"},
            responses = {
                    @ApiResponse(description = "Sucess",
                            responseCode = "200",
                            content =
                            @Content(schema = @Schema(implementation = WorkoutDTO.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    WorkoutDTO create(@RequestBody WorkoutDTO workoutDTO);

    @Operation(summary = "Update a Workout's information",
            description = "Updating a Workout's information by passing in a JSON, XML or YML representation of the Workouts.",
            tags = {"Workouts"},
            responses = {
                    @ApiResponse(description = "Sucess",
                            responseCode = "200",
                            content =
                            @Content(schema = @Schema(implementation = WorkoutDTO.class))
                    ),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    WorkoutDTO update(@RequestBody WorkoutDTO workoutDTO);

    @Operation(summary = "Delete a Workout",
            description = "Delete's a specific Workout by their ID.",
            tags = {"Workouts"},
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
