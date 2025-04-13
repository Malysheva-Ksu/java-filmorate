package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PositiveDurationValidator implements ConstraintValidator<PositiveDuration, Number> {
    @Override
    public void initialize(PositiveDuration constraintAnnotation) {
    }

    @Override
    public boolean isValid(Number value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.longValue() > 0;
    }
}