package br.com.application.controllers.docs;

import br.com.application.data.dto.security.AccountCredentialsDTO;
import br.com.application.data.dto.security.TokenDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthControllerDocs {

    @Operation(summary = "Authenticates an user and returns a token",
            description = "Authenticates a specific user and returns an access token",
            tags = {"Authentication"},
            responses = {
                    @ApiResponse(description = "Sign-in successful", responseCode = "200", content = @Content(schema = @Schema(implementation = TokenDTO.class))),
                    @ApiResponse(description = "Invalid username or password format", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Invalid credentials", responseCode = "401", content = @Content),
                    @ApiResponse(description = "User not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    ResponseEntity<?> sigIn(@RequestBody AccountCredentialsDTO credentials);

    @Operation(summary = "Refresh for authenticated user and returns a token",
            description = "Refreshes an authenticated user's access token",
            tags = {"Authentication"},
            responses = {
                    @ApiResponse(description = "Token refreshed successfully", responseCode = "200", content = @Content(schema = @Schema(implementation = TokenDTO.class))),
                    @ApiResponse(description = "Invalid username or refresh token", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Invalid or expired refresh token", responseCode = "401", content = @Content),
                    @ApiResponse(description = "User not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    ResponseEntity<?> refreshToken(String username, String refreshToken);

    @Operation(summary = "Creates an authenticated user with the encrypted password",
            description = "Creates a new authenticated user with the encrypted password",
            tags = {"Authentication"},
            responses = {
                    @ApiResponse(description = "User created successfully", responseCode = "201", content = @Content(schema = @Schema(implementation = AccountCredentialsDTO.class))),
                    @ApiResponse(description = "Invalid user data", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Username already exists", responseCode = "409", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    ResponseEntity<AccountCredentialsDTO> create(@RequestBody AccountCredentialsDTO credentialsDTO);
}