package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private int currentId = 1;

    @PostMapping
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
        if (!film.isValidDuration()) {
            log.warn("Попытка добавить фильм с некорректной продолжительностью: {}", film);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        film.setId(currentId++);
        films.put(film.getId(), film);
        log.info("Добавлен новый фильм: {}", film);
        return ResponseEntity.ok(film);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Film> updateFilm(@PathVariable int id, @Valid @RequestBody Film film) {
        if (!films.containsKey(id)) {
            log.warn("Попытка обновления несуществующего фильма с ID {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        film.setId(id);
        films.put(id, film);
        log.info("Обновлен фильм с ID {}: {}", id, film);
        return ResponseEntity.ok(film);
    }

    @GetMapping
    public ResponseEntity<List<Film>> getAllFilms() {
        List<Film> list = films.values().stream().collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }
}