package br.com.application.exception;

public class FileNotExportingException extends RuntimeException
{
    public FileNotExportingException(String message) {
        super(message);
    }

    public FileNotExportingException(String message, Throwable cause) {
        super(message, cause);
    }
}
