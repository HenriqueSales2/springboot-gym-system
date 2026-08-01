package br.com.application.controllers.docs;

import br.com.application.data.dto.request.EmailRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface EmailControllerDocs {

    @Operation(summary = "Send an e-Mail",
            description = "Sends an e-mail by providing recipient, subject and message content",
            tags = {"Email"},
            responses = {
                    @ApiResponse(description = "E-mail sent successfully", responseCode = "200", content = @Content),
                    @ApiResponse(description = "Invalid e-mail request data", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Authentication required", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Failed to send e-mail", responseCode = "500", content = @Content),
            }
    )
    ResponseEntity<String> sendEmail(EmailRequestDTO emailRequestDTO);

    @Operation(summary = "Send an e-Mail with Attachment",
            description = "Sends an e-mail with an attachment by providing recipient, subject, message content and file",
            tags = {"Email"},
            responses = {
                    @ApiResponse(description = "E-mail with attachment sent successfully", responseCode = "200", content = @Content),
                    @ApiResponse(description = "Invalid e-mail request data or attachment", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Authentication required", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Failed to send e-mail with attachment", responseCode = "500", content = @Content),
            }
    )
    ResponseEntity<String> sendEmailWithAttachment(String emailRequestJSON, MultipartFile attachment);
}