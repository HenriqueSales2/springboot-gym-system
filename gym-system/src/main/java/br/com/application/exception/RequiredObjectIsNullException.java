package br.com.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // atribuíndo o status de BAD REQUEST caso caia nessa exceção
public class RequiredObjectIsNullException extends RuntimeException {

    public RequiredObjectIsNullException() {
        super("It is not allowed to persist a null object!");
    }

    public RequiredObjectIsNullException(String message) {
        super(message);
    }



}
