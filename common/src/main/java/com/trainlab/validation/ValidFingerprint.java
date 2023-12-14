package com.trainlab.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Pattern(regexp = "^[\\da-f]{40}$")
public @interface ValidFingerprint {
    String message() default "Incorrect fingerprint.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
