package com.trainlab.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AccessControlViolated extends RuntimeException {

    public AccessControlViolated(String message) {
        super(message);
    }
}