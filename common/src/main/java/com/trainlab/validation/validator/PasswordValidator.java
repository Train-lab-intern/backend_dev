package com.trainlab.validation.validator;

import com.trainlab.validation.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?!.*[а-яА-Я]).{8,256}$");
    }
}
