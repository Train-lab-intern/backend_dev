package com.trainlab.validation;
import com.trainlab.validation.validator.ValidNameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidNameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidName {
    String message() default "Invalid Name or Surname. They must be typed in Latin or Cyrillic letters,  consist of at least 1 character";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    boolean nullable() default false;
}
