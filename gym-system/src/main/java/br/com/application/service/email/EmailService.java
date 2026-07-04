package br.com.application.service.email;

import br.com.application.config.EmailConfig;
import br.com.application.data.dto.request.EmailRequestDTO;
import br.com.application.mail.EmailSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class EmailService {

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private EmailConfig emailConfig;

    public void sendSimpleEmail(EmailRequestDTO emailRequestDTO) {
        emailSender
                .to(emailRequestDTO.getTo())
                .withSubject(emailRequestDTO.getSubject())
                .withMessage(emailRequestDTO.getBody())
                .send(emailConfig);
    }

    public void sendEmailWithAttachment(String emailRequestJSON, MultipartFile attachment) {
        File tempFile = null;

        try {
            EmailRequestDTO emailRequestDTO = new ObjectMapper()
                    .readValue(emailRequestJSON, EmailRequestDTO.class);
            tempFile = File.createTempFile("attachment",  attachment.getOriginalFilename());
            attachment.transferTo(tempFile);

            emailSender
                    .to(emailRequestDTO.getTo())
                    .withSubject(emailRequestDTO.getSubject())
                    .withMessage(emailRequestDTO.getBody())
                    .attach(tempFile.getAbsolutePath())
                    .send(emailConfig);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing email request JSON!", e);
        } catch (IOException e) {
            throw new RuntimeException("Error processing the attachment!", e);
        } finally {
            if(tempFile != null && tempFile.exists()) tempFile.delete();
        }
    }
}