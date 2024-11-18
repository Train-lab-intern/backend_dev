package com.trainlab.api.exception;

import com.trainlab.exception.*;
import jakarta.persistence.EntityNotFoundException;
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
public class MainExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(
            ObjectNotFoundException ex) {
        List<String> details = new ArrayList<>();
        details.add(Arrays.toString(ex.getStackTrace()));
        ApiError err = new ApiError(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                details);
        return ResponseEntityBuilder.build(err);
    }

    @ExceptionHandler(UsernameGenerationException.class)
    public ResponseEntity<Object> handleUsernameGenerationException(
            UsernameGenerationException ex
    ) {
        List<String> details = List.of(Arrays.toString(ex.getStackTrace()));
        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                details);
        return ResponseEntityBuilder.build(error);
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public ResponseEntity<Object> handleTokenNotFoundException(
            RefreshTokenNotFoundException ex
    ) {
        List<String> details = new ArrayList<>();
        details.add(Arrays.toString(ex.getStackTrace()));

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                details);

        return ResponseEntityBuilder.build(apiError);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<Object> handleTokenNotFoundException(
            TokenExpiredException ex
    ) {
        List<String> details = new ArrayList<>();
        details.add(Arrays.toString(ex.getStackTrace()));

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                details);

        return ResponseEntityBuilder.build(apiError);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleTokenNotFoundException(
            EntityNotFoundException ex
    ) {
        List<String> details = new ArrayList<>();
        details.add(Arrays.toString(ex.getStackTrace()));

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                details);

        return ResponseEntityBuilder.build(apiError);
    }

    @ExceptionHandler({LoginValidationException.class})
    public ResponseEntity<Object> handleLoginValidationException(
            LoginValidationException ex
    ) {
        ArrayList<String> details = new ArrayList<>();
        details.add(Arrays.toString(ex.getStackTrace()));
        ApiError apiError = new ApiError(LocalDateTime.now(), HttpStatus.UNAUTHORIZED, ex.getMessage(), details);

        return ResponseEntityBuilder.build(apiError);
    }
}
