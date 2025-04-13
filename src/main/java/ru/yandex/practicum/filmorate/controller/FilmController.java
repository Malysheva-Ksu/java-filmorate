package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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

    private final Map<Long, Film> films = new HashMap<>();
    private Long currentId = 1L;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
        film.setId(currentId++);
        films.put(film.getId(), film);
        log.info("Добавлен новый фильм: {}", film);
        return ResponseEntity.ok(film);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("Попытка обновления несуществующего фильма с ID {}", film.getId());
            throw new RuntimeException("Фильм не найден. ID: " + film.getId());
        }
        Film existingFilm = films.get(film.getId());
        existingFilm.setName(film.getName());
        existingFilm.setDescription(film.getDescription());
        existingFilm.setReleaseDate(film.getReleaseDate());
        existingFilm.setDurationInSeconds(film.getDurationInSeconds());
        films.put(existingFilm.getId(), existingFilm);
        log.info("Обновлен фильм с ID {}: {}", film.getId(), existingFilm);
        return ResponseEntity.ok(existingFilm);
    }

    @GetMapping
    public ResponseEntity<List<Film>> getAllFilms() {
        List<Film> list = films.values().stream().collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }
}