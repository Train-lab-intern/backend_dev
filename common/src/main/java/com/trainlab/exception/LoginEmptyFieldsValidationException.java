package com.trainlab.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LoginEmptyFieldsValidationException extends RuntimeException {
    public LoginEmptyFieldsValidationException(String message) {
        super(message);
    }
}
