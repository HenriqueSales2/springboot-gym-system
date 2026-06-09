package br.com.application.controllers.docs;

import br.com.application.data.dto.UploadFileDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "File Endpoint")
public interface FileControllerDocs {

    @Operation(summary = "Upload a File",
            description = "Upload a specific File",
            tags = {"Files"},
            responses = {
                    @ApiResponse(description = "Sucess",
                            responseCode = "200",
                            content =
                            @Content(schema = @Schema(implementation = UploadFileDTO.class))
                    ),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    UploadFileDTO uploadFile(MultipartFile file);

    @Operation(summary = "Upload a Multiples Files",
            description = "Upload a Multiples Files",
            tags = {"Files"},
            responses = {
                    @ApiResponse(description = "Sucess",
                            responseCode = "200",
                            content =
                            @Content(schema = @Schema(implementation = UploadFileDTO.class))
                    ),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    List<UploadFileDTO> uploadMultiplesFiles(MultipartFile[] files);

    @Operation(summary = "Download Files",
            description = "Download a Multiples Files",
            tags = {"Files"},
            responses = {
                    @ApiResponse(description = "Sucess",
                            responseCode = "200",
                            content =
                            @Content(schema = @Schema(implementation = UploadFileDTO.class))
                    ),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    ResponseEntity<Resource> downloadFile(String fileName,
                                          HttpServletRequest request);
}