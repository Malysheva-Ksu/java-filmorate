package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.List;
import java.util.Set;

@Service
public class FilmService {

    private static final Logger log = LoggerFactory.getLogger(FilmService.class);

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("inMemoryFilmStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addFilm(Film film) {
        Film addedFilm = filmStorage.addFilm(film);
        log.info("Добавлен новый фильм: {}", addedFilm);
        return addedFilm;
    }

    public Film updateFilm(Film film) {
        Film updatedFilm = filmStorage.updateFilm(film);
        log.info("Обновлен фильм: {}", updatedFilm);
        return updatedFilm;
    }

    public List<Film> getAllFilms() {
        List<Film> films = filmStorage.getAllFilms();
        log.info("Получено {} фильмов", films.size());
        return films;
    }

    public Set<Long> addLike(Long filmId, Long userId) {
        filmStorage.addLike(filmId, userId);
        log.info("Лайк добавлен для фильма с ID {} от пользователя {}", filmId, userId);
        Film film = filmStorage.getFilmById(filmId);
        Set<Long> likes = film.getLikes();

        return likes;
    }

    public Set<Long> removeLike(Long filmId, Long userId) {
        filmStorage.removeLike(filmId, userId);
        log.info("Лайк удалён для фильма с ID {} от пользователя {}", filmId, userId);
        Film film = filmStorage.getFilmById(filmId);
        Set<Long> likes = film.getLikes();

        return likes;
    }

    public List<Film> getTopFilms(Integer count) {
        List<Film> topFilms = filmStorage.getTopFilms().stream().limit(count).toList();
        log.info("Возвращено {} популярных фильмов", topFilms.size());
        return topFilms;
    }

    public Film getFilmById(Long filmId) {
        Film film = filmStorage.getFilmById(filmId);
        return film;
    }
}