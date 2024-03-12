package com.trainlab.validation.validator;

import com.trainlab.validation.ValidPassword;
import com.trainlab.validation.ValidPasswordNull;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidatorNull implements ConstraintValidator<ValidPasswordNull, String> {
    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        if (password == null) {
            return true; // Null values are considered valid if nullable is true
        }
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?!.*[а-яА-Я]).{8,256}$");
    }
}
