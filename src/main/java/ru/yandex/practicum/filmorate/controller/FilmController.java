package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private Map<Integer, Film> films = new HashMap<>();
    private int currentId = 1;

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        if (film.isValidDuration()) {
            film.setId(currentId++);
            films.put(film.getId(), film);
            log.info("Добавлен новый фильм: {}", film);
        } else {
            return null;
        }
        return film;
    }


    @PutMapping("/{id}")
    public Film updateFilm(@PathVariable int id, @Valid @RequestBody Film film) {
        if (films.containsKey(id)) {
            film.setId(id);
            films.put(id, film);
            log.info("Обновлен фильм с ID {}: {}", id, film);
            return film;
        }
        log.warn("Попытка обновления несуществующего фильма с ID {}", id);
        return null;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return films.values().stream().collect(Collectors.toList());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
            log.error("Ошибка валидации для поля {}: {}", error.getField(), error.getDefaultMessage());
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}