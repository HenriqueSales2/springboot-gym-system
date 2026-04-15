package br.com.application.exception;

import java.util.Date;

public record ExceptionResponse(Date timestamp, String message, String details) {}
