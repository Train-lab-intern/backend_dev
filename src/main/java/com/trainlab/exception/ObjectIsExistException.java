package com.trainlab.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ObjectIsExistException extends RuntimeException {

    public ObjectIsExistException(String message) {
        super(message);
    }
}
