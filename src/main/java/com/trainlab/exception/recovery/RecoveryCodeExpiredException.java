package com.trainlab.exception.recovery;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
public class RecoveryCodeExpiredException extends RuntimeException {

    public RecoveryCodeExpiredException(String errorMessage) {
        super(errorMessage);
    }
}
