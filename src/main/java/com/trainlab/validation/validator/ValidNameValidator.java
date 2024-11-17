package com.trainlab.validation.validator;

import com.trainlab.validation.ValidName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ValidNameValidator  implements ConstraintValidator<ValidName,String> {
    private boolean nullable;

    @Override
    public void initialize(ValidName constraintAnnotation) {
        nullable = constraintAnnotation.nullable();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null) {
            return nullable; // Возвращаем true, если значение null разрешено
        }


        // Регулярное выражение для проверки на кириллицу или латиницу
        String regex = "^[а-яА-Я]+$|^[a-zA-Z]+$";
        Pattern pattern = Pattern.compile(regex);

        // Если строка соответствует шаблону
        return pattern.matcher(value).matches();
    }
}
