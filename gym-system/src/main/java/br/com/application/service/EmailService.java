package br.com.application.service;

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
        // como é só um email não é necessário desserializar (fica mais simples, apenas passando os parametros)
        emailSender // enviando o email
                .to(emailRequestDTO.getTo()) // para alguém
                .withSubject(emailRequestDTO.getSubject()) // com um assunto
                .withMessage(emailRequestDTO.getBody()) // com uma mensagem
                .send(emailConfig);// passando as configurações de email
    }

    public void sendEmailWithAttachment(String emailRequestJSON, MultipartFile attachment) {
        File tempFile = null;

        try {
            EmailRequestDTO emailRequestDTO = new ObjectMapper() // desserializando o email e fazendo com que ele vire um objeto
                    .readValue(emailRequestJSON, EmailRequestDTO.class);
            tempFile = File.createTempFile("attachment",  attachment.getOriginalFilename()); // gravando um arquivo temporariamente em disco para depois enviá-los
            attachment.transferTo(tempFile);

            emailSender // enviando o email
                    .to(emailRequestDTO.getTo()) // para alguém
                    .withSubject(emailRequestDTO.getSubject()) // com um assunto
                    .withMessage(emailRequestDTO.getBody()) // com uma mensagem
                    .attach(tempFile.getAbsolutePath()) // e o arquivo que queira mandar para esse alguém
                    .send(emailConfig); // passando as configurações de email

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing email request JSON!", e); // exceção caso ocorra algum erro na hora de parsear o objeto
        } catch (IOException e) {
            throw new RuntimeException("Error processing the attachment!", e); // exceção caso ocorra algum erro relacionado ao arquivo
        } finally {
            if(tempFile != null && tempFile.exists()) tempFile.delete(); // deletando o arquivo temporário
        }
    }
}