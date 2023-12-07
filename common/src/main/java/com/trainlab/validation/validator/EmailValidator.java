package com.trainlab.validation.validator;

import com.trainlab.validation.Email;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class EmailValidator implements ConstraintValidator<Email, String> {
    private final static String IPv4_PATTERN = "\\[((25[0-5]|2[0-4]\\d|[01]?\\d?\\d)(\\.)){3}(25[0-5]|2[0-4]\\d|[01]?\\d?\\d)]";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.matches("^[A-Za-z0-9._%+\\-'`]+@(" + IPv4_PATTERN +  "|([A-Za-z0-9.-]+\\.[A-Za-z]{2,4}))$");
    }
}
