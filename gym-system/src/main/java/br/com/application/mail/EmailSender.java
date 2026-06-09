package br.com.application.mail;

import br.com.application.config.EmailConfig;
import br.com.application.exception.InvalidEmailException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;

@Component
public class EmailSender implements Serializable {

    Logger logger = LoggerFactory.getLogger(EmailSender.class);

    private final JavaMailSender mailSender;

    private String to;
    private String subject;
    private String body;
    private ArrayList<InternetAddress> recipients = new ArrayList<>();
    private File attachment;

    public EmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public EmailSender to(String to) {
        this.to = to;
        this.recipients = getRecipients(to);
        return this;
    }

    public EmailSender withSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public EmailSender withMessage(String body) {
        this.body = body;
        return this;
    }

    public EmailSender attach(String fileDir) {
        this.attachment = new File(fileDir);
        return this;
    }

    public void send(EmailConfig config) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(config.getUsername());
            helper.setTo(recipients.toArray(new InternetAddress[0]));
            helper.setSubject(subject);
            helper.setText(body, true);

            if (attachment != null) {
                helper.addAttachment(attachment.getName(), attachment);
            }
            mailSender.send(message);
            logger.info("Email sent to %s with the subject '%s'%n", to, subject);
            reset();

        } catch (Exception e) {
            throw new RuntimeException("Error sending the email", e);
        }
    }

    private void reset() {
        this.to = null;
        this.subject = null;
        this.body = null;
        this.recipients = null;
        this.attachment = null;
    }

    private ArrayList<InternetAddress> getRecipients(String to) {
        String toWithoutSpaces = to.replaceAll("\\s", ""); // caso o usuário por algum motivo tenha colocado espaço no email vai ser tratado
        StringTokenizer tokenizer = new StringTokenizer(toWithoutSpaces, ";");
        ArrayList<InternetAddress> recipientsList = new ArrayList<>();

        while (tokenizer.hasMoreElements()) {
            try {
                recipientsList.add(new InternetAddress(tokenizer.nextElement().toString()));
            } catch (Exception e) {
                throw new InvalidEmailException("Invalid email address: ", e);
            }
        }
        return recipientsList;
    }
}