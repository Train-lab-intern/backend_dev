package com.trainlab.util;

import com.trainlab.exception.ValidationException;
import org.springframework.validation.BindingResult;

import java.util.Objects;

public class ValidationUtil {

    public static void isRequestValid(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            throw new ValidationException(errorMessage);
        }
    }

}
