package com.trainlab.validation;

import com.trainlab.validation.validator.PasswordValidator;
import com.trainlab.validation.validator.PasswordValidatorNull;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {PasswordValidatorNull.class})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
public @interface ValidPasswordNull {
    String message() default "Invalid password. The password must be typed in Latin letters, consist of at least 8 characters and contain at least one lowercase and one uppercase character";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}