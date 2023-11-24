package com.trainlab.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?!.*[а-яА-Я]).{8,}$")
@ReportAsSingleViolation
public @interface ValidPassword {
    String message() default "Password must contain 8 characters and at least one lowercase and one uppercase character";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}