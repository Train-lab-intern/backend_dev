package com.trainlab.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UsernameGenerationException extends RuntimeException {
    public UsernameGenerationException(String errorMessage) {
        super(errorMessage);
    }
}
