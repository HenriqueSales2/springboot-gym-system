package br.com.application.controllers.email;

import br.com.application.config.SecurityConfig;
import br.com.application.controllers.docs.EmailControllerDocs;
import br.com.application.data.dto.request.EmailRequestDTO;
import br.com.application.service.email.EmailService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/email/v1")
@Tag(name = "Email", description = "Endpoints for sends an e-Mail")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class EmailController implements EmailControllerDocs {

    @Autowired
    private EmailService service;

    @PostMapping
    @Override
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDTO emailRequestDTO) {
        service.sendSimpleEmail(emailRequestDTO);
        return new ResponseEntity<>("e-Mail sent with success!", HttpStatus.OK);
    }

    @PostMapping(
            value = "/withAttachment",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Override
    public ResponseEntity<String> sendEmailWithAttachment(
            @RequestParam("emailRequest") String emailRequestJSON,
            @RequestParam("attachment") MultipartFile attachment) {
        service.sendEmailWithAttachment(emailRequestJSON, attachment);
        return new ResponseEntity<>("e-Mail with attachment sent with successfully!", HttpStatus.OK);
    }
}