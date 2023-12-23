package com.trainlab.validation;

import com.trainlab.validation.validator.EmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {EmailValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Email {

    String message() default "Invalid login or password.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
