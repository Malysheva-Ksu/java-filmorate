package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final MpaService mpaService;

    @Autowired
    public FilmController(FilmService filmService, MpaService mpaService) {
        this.filmService = filmService;
        this.mpaService = mpaService;
    }

    @PostMapping
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
        if (film.getMpa() != null && film.getMpa().getId() != null) {
            try {
                mpaService.checkMpaExists(film.getMpa().getId());
            } catch (MpaNotFoundException e) {
                log.error("Ошибка при создании фильма: {}", e.getMessage());
                throw e;
            }
        }
        Film createdFilm = filmService.addFilm(film);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFilm);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        Film updatedFilm = filmService.updateFilm(film);
        return ResponseEntity.ok(updatedFilm);
    }

    @GetMapping
    public ResponseEntity<List<Film>> getAllFilms() {
        List<Film> films = filmService.getAllFilms();
        return ResponseEntity.ok(films);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public ResponseEntity<Set<Long>> addLike(@PathVariable Long filmId, @PathVariable Long userId) {
        Set<Long> likes = filmService.addLike(filmId, userId);
        return ResponseEntity.ok(likes);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public ResponseEntity<Set<Long>> removeLike(@PathVariable Long filmId, @PathVariable Long userId) {
        Set<Long> likes = filmService.removeLike(filmId, userId);
        return ResponseEntity.ok(likes);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Film>> getTopFilms(@RequestParam(name = "count", defaultValue = "10") int count) {
        List<Film> topFilms = filmService.getTopFilms(count);
        return ResponseEntity.ok(topFilms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable("id") Long id) {
        Film film = filmService.getFilmById(id);
        return ResponseEntity.ok(film);
    }
}