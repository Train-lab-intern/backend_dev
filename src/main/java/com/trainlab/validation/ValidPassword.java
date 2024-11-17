package com.trainlab.validation;

import com.trainlab.validation.validator.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = {PasswordValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
public @interface ValidPassword {
    String message() default "Invalid password. The password must be typed in Latin letters, consist of at least 8 characters and contain at least one lowercase and one uppercase character";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}