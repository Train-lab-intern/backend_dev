package com.trainlab.exception.recovery;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalRecoveryCodeException extends RuntimeException {

    public IllegalRecoveryCodeException(String errorMessage) {
        super(errorMessage);
    }
}
