package com.trainlab.api.exception.recovery;

import com.trainlab.exception.ApiError;
import com.trainlab.exception.ResponseEntityBuilder;
import com.trainlab.exception.recovery.IllegalRecoveryCodeException;
import com.trainlab.exception.recovery.RateLimitExceededException;
import com.trainlab.exception.recovery.RecoveryCodeExpiredException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RecoveryCodeExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<Object> handleRateLimitExceededException(RateLimitExceededException ex) {
        List<String> details = new ArrayList<>();
        details.add(Arrays.toString(ex.getStackTrace()));
        ApiError err = new ApiError(
                LocalDateTime.now(),
                HttpStatus.TOO_MANY_REQUESTS,
                ex.getMessage(),
                details);
        return ResponseEntityBuilder.build(err);
    }

    @ExceptionHandler(IllegalRecoveryCodeException.class)
    public ResponseEntity<Object> handleIllegalRecoveryCodeException(IllegalRecoveryCodeException ex) {
        List<String> details = new ArrayList<>();
        details.add(Arrays.toString(ex.getStackTrace()));
        ApiError err = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                details);
        return ResponseEntityBuilder.build(err);
    }

    @ExceptionHandler(RecoveryCodeExpiredException.class)
    public ResponseEntity<Object> handleRecoveryCodeExpiredException(RecoveryCodeExpiredException ex) {
        List<String> details = new ArrayList<>();
        details.add(Arrays.toString(ex.getStackTrace()));
        ApiError err = new ApiError(
                LocalDateTime.now(),
                HttpStatus.REQUEST_TIMEOUT,
                ex.getMessage(),
                details);
        return ResponseEntityBuilder.build(err);
    }
}
