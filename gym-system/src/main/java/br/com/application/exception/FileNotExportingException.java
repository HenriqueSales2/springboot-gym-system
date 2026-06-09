package br.com.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FileNotExportingException extends RuntimeException {

    public FileNotExportingException(String message) {
        super(message);
    }

    public FileNotExportingException(String message, Throwable cause) {
        super(message, cause);
    }
}
