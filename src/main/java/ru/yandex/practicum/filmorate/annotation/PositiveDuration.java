package ru.yandex.practicum.filmorate.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PositiveDurationValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PositiveDuration {
    String message() default "Продолжительность фильма должна быть положительным числом.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}