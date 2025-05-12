package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class FilmTests {

    private final Validator validator;

    public FilmTests() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    public void shouldNotValidateWhenNameIsEmpty() {
        Film film = new Film();
        film.setName("");
        film.setDescription("A valid description.");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDurationInSeconds(200L);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("Название не может быть пустым.");
    }

    @Test
    public void shouldNotValidateWhenDescriptionIsTooLong() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("A".repeat(201));
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDurationInSeconds(200L);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("Максимальная длина описания — 200 символов.");
    }

    @Test
    public void shouldNotValidateWhenReleaseDateIsTooEarly() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("A valid description.");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDurationInSeconds(200L);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("Дата релиза не может быть раньше 28 декабря 1895 года.");
    }

    @Test
    public void shouldNotValidateWhenDurationIsNegative() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("A valid description.");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDurationInSeconds(-200L);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("Продолжительность фильма должна быть положительным числом.");
    }

    @Test
    public void shouldValidateSuccessfully() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("A valid description.");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDurationInSeconds(200L);

        Mpa mpa = new Mpa(1L, "G");
        film.setMpa(mpa);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertThat(violations).isEmpty();
    }
}