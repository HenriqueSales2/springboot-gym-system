package br.com.application.controllers.auth;


import br.com.application.controllers.docs.AuthControllerDocs;
import br.com.application.data.dto.security.AccountCredentialsDTO;
import br.com.application.service.auth.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "Endpoints for Authentication")
@RestController
@RequestMapping("/auth")
public class AuthController implements AuthControllerDocs {

    @Autowired
    private AuthService service;

    @PostMapping("/signin")
    @Override
    public ResponseEntity<?> sigIn(@RequestBody AccountCredentialsDTO credentials) {
        if(credentialsValidations(credentials)) return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Invalid client request!");

        var token = service.sigIn(credentials);
        if (token == null) return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Invalid client request!");

        return token;
    }

    @PutMapping("/refresh/{username}")
    @Override
    public ResponseEntity<?> refreshToken(@PathVariable("username") String username,
                                          @RequestHeader("Authorization") String refreshToken) {
        if (parametersAreInvalid(username, refreshToken)) return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Invalid client request!");

        var token = service.refreshToken(username, refreshToken);
        if (token == null) return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Invalid client request!");

        return token;
    }

    @PostMapping(value = "/createUser",
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
    public ResponseEntity<AccountCredentialsDTO> create(@RequestBody AccountCredentialsDTO credentials) {
        var user = service.create(credentials);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    // método para verificar se os parametros de "refreshToken" estão válidos
    private boolean parametersAreInvalid(String username, String refreshToken) {
        return StringUtils.isBlank(username) ||
                StringUtils.isBlank(refreshToken);
    }

    // método para fazer validações das credencias do cliente
    private static boolean credentialsValidations(AccountCredentialsDTO credentials) {
        return credentials == null ||
                StringUtils.isBlank(credentials.getUsername()) ||
                StringUtils.isBlank(credentials.getPassword());
    }
}